package beertech.becks.api.service.impl;

import beertech.becks.api.entities.Account;
import beertech.becks.api.entities.PaymentSlip;
import beertech.becks.api.entities.Transaction;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.account.AccountDoesNotHaveEnoughBalanceException;
import beertech.becks.api.model.TypeOperation;
import beertech.becks.api.repositories.AccountRepository;
import beertech.becks.api.repositories.TransactionRepository;
import beertech.becks.api.service.AccountService;
import beertech.becks.api.service.TransactionService;
import beertech.becks.api.tos.request.TransferRequestTO;
import beertech.becks.api.tos.response.StatementResponseTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static beertech.becks.api.model.PaymentCategory.OTHERS;
import static beertech.becks.api.model.TypeOperation.*;
import static java.time.LocalDateTime.now;

@Service
public class TransactionServiceImpl implements TransactionService {
	
	private final TransactionRepository transactionRepository;
	private final AccountRepository accountRepository;
	private final AccountService accountService;

	public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository,
			AccountService accountService) {
		this.transactionRepository = transactionRepository;
		this.accountRepository = accountRepository;
		this.accountService = accountService;
	}

	@Override
	public StatementResponseTO getStatements(String accountCode) throws AccountDoesNotExistsException {
		Account account = findAccountByCode(accountCode);

		return StatementResponseTO.builder().accountStatements(transactionRepository.findByAccountId(account.getId()))
				.balance(account.getBalance()).build();
	}

	@Override
	public Account createDeposit(String accountCode, BigDecimal value) throws AccountDoesNotExistsException {
		Account account = findAccountByCode(accountCode);

		transactionRepository.save(Transaction.builder().typeOperation(DEPOSITO).dateTime(now()).valueTransaction(value)
				.accountId(account.getId()).build());

		account.setBalance(account.getBalance().add(value));
		accountRepository.save(account);

		return account;
	}

	@Override
	public Account createWithdrawal(String accountCode, BigDecimal value)
			throws AccountDoesNotExistsException, AccountDoesNotHaveEnoughBalanceException {
		Account account = findAccountByCode(accountCode);
		accountService.checkAvailableBalance(account.getBalance(), value);

		transactionRepository.save(Transaction.builder().typeOperation(SAQUE).dateTime(now())
				.valueTransaction(value.negate()).accountId(account.getId()).build());

		account.setBalance(account.getBalance().subtract(value));
		accountRepository.save(account);

		return account;
	}

	@Override
	public Account createTransfer(String accountCode, TransferRequestTO transferRequestTO, TypeOperation typeOperation)
			throws AccountDoesNotExistsException, AccountDoesNotHaveEnoughBalanceException {
		Account originAccount = findAccountByCode(accountCode);
		Account destinationAccount = findAccountByCode(transferRequestTO.getDestinationAccountCode());
		accountService.checkAvailableBalance(originAccount.getBalance(), transferRequestTO.getValue());

		List<Transaction> allTransactionsToSave = new ArrayList<>();

		LocalDateTime currentDate = now();

		// Debit
		allTransactionsToSave.add(Transaction.builder().typeOperation(typeOperation).dateTime(currentDate)
				.paymentCategory(OTHERS).valueTransaction(transferRequestTO.getValue().negate()).accountId(originAccount.getId()).build());
		originAccount.setBalance(originAccount.getBalance().subtract(transferRequestTO.getValue()));

		// Credit
		allTransactionsToSave.add(Transaction.builder().typeOperation(typeOperation).dateTime(currentDate)
				.paymentCategory(OTHERS).valueTransaction(transferRequestTO.getValue()).accountId(destinationAccount.getId()).build());
		destinationAccount.setBalance(destinationAccount.getBalance().add(transferRequestTO.getValue()));

		transactionRepository.saveAll(allTransactionsToSave);

		List<Account> allAccountsToSave = new ArrayList<>();
		allAccountsToSave.add(originAccount);
		allAccountsToSave.add(destinationAccount);
		accountRepository.saveAll(allAccountsToSave);

		return originAccount;
	}

	@Override
	public Account createPayment(PaymentSlip paymentSlip) throws AccountDoesNotExistsException, AccountDoesNotHaveEnoughBalanceException {
		TransferRequestTO transferRequestTO = new TransferRequestTO();
		transferRequestTO.setDestinationAccountCode(paymentSlip.getDestinationAccountCode());
		transferRequestTO.setValue(paymentSlip.getValue());

		return createTransfer(paymentSlip.getOriginAccountCode(), transferRequestTO, PAGAMENTO);
	}

	@Override
	public Account createPaymentToExternalBank(PaymentSlip paymentSlip)
			throws AccountDoesNotExistsException, AccountDoesNotHaveEnoughBalanceException {

		Account account = findAccountByCode(paymentSlip.getOriginAccountCode());
		accountService.checkAvailableBalance(account.getBalance(), paymentSlip.getValue());

		transactionRepository.save(Transaction.builder().typeOperation(PAGAMENTO)
				.paymentCategory(paymentSlip.getCategory()).dateTime(now())
				.valueTransaction(paymentSlip.getValue().negate()).accountId(account.getId()).build());

		account.setBalance(account.getBalance().subtract(paymentSlip.getValue()));
		accountRepository.save(account);

		return account;
	}


	private Account findAccountByCode(String accountCode) throws AccountDoesNotExistsException {
		return accountRepository.findByCode(accountCode).orElseThrow(AccountDoesNotExistsException::new);
	}

}
