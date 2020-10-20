package beertech.becks.api.service.impl;

import static beertech.becks.api.model.TypeOperation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import beertech.becks.api.entities.Account;
import beertech.becks.api.entities.Transaction;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.repositories.AccountRepository;
import beertech.becks.api.repositories.TransactionRepository;
import beertech.becks.api.service.TransactionService;
import beertech.becks.api.tos.request.TransferRequestTO;
import beertech.becks.api.tos.response.StatementResponseTO;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public StatementResponseTO getStatements(String accountCode) throws AccountDoesNotExistsException {
		Account acc = accountRepository.findByCode(accountCode).orElseThrow(AccountDoesNotExistsException::new);

		StatementResponseTO accountStatement = new StatementResponseTO();
		accountStatement.setAccountStatements(transactionRepository.findByAccountId(acc.getId()));
		accountStatement.setBalance(acc.getBalance());

		return accountStatement;
	}

	@Override
	public Account createDeposit(String accountCode, BigDecimal value) throws AccountDoesNotExistsException {
		Account acc = accountRepository.findByCode(accountCode).orElseThrow(AccountDoesNotExistsException::new);

		transactionRepository.save(Transaction.builder().typeOperation(DEPOSITO).dateTime(LocalDateTime.now())
				.valueTransaction(value).accountId(acc.getId()).build());

		acc.setBalance(acc.getBalance().add(value));
		accountRepository.save(acc);
		return acc;
	}

	@Override
	public Account createWithdrawal(String accountCode, BigDecimal value) throws AccountDoesNotExistsException {
		Account acc = accountRepository.findByCode(accountCode).orElseThrow(AccountDoesNotExistsException::new);

		transactionRepository.save(Transaction.builder().typeOperation(SAQUE).dateTime(LocalDateTime.now())
				.valueTransaction(value.negate()).accountId(acc.getId()).build());

		acc.setBalance(acc.getBalance().subtract(value));
		accountRepository.save(acc);
		return acc;
	}

	@Override
	public Account createTransfer(String accountCode, TransferRequestTO transferRequestTO)
			throws AccountDoesNotExistsException {
		LocalDateTime now = LocalDateTime.now();
		Account originAcc = accountRepository.findByCode(accountCode).orElseThrow(AccountDoesNotExistsException::new);
		Account destinationAcc = accountRepository.findByCode(transferRequestTO.getDestinationAccountCode())
				.orElseThrow(AccountDoesNotExistsException::new);

		List<Transaction> transactionsToSave = new ArrayList<>();

		Transaction debitTransaction = Transaction.builder().typeOperation(TRANSFERENCIA)
				.valueTransaction(transferRequestTO.getValue().negate()).dateTime(now).accountId(originAcc.getId())
				.build();

		Transaction creditTransaction = Transaction.builder().typeOperation(TRANSFERENCIA)
				.valueTransaction(transferRequestTO.getValue()).dateTime(now).accountId(destinationAcc.getId()).build();

		transactionsToSave.add(debitTransaction);
		transactionsToSave.add(creditTransaction);
		transactionRepository.saveAll(transactionsToSave);

		List<Account> accountsToSave = new ArrayList<>();

		originAcc.setBalance(originAcc.getBalance().subtract(transferRequestTO.getValue()));
		accountsToSave.add(originAcc);

		destinationAcc.setBalance(destinationAcc.getBalance().add(transferRequestTO.getValue()));
		accountsToSave.add(destinationAcc);

		accountRepository.saveAll(accountsToSave);

		return originAcc;
	}

}
