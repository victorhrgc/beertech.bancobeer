package beertech.becks.api.service.impl;

import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.model.TypeOperation;
import beertech.becks.api.repositories.AccountRepository;
import beertech.becks.api.tos.response.BalanceResponseTO;
import beertech.becks.api.entities.Transaction;
import beertech.becks.api.repositories.TransactionRepository;
import beertech.becks.api.service.TransactionService;
import beertech.becks.api.tos.request.TransactionRequestTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static beertech.becks.api.model.TypeOperation.*;
import static java.time.ZonedDateTime.now;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public void createTransaction(TransactionRequestTO transactionTO) throws AccountDoesNotExistsException {
		validateAccounts(transactionTO.getOriginAccountCode(), transactionTO.getDestinationAccountCode());

		if (TRANSFERENCIA.getDescription().equals(transactionTO.getOperation())) {
			createTransferTransaction(transactionTO);
		} else {
			createSelfTransaction(transactionTO);
		}
	}

	/**
	 * This method validates if the informed accounts on this transaction exist
	 * 
	 * @param originAccountCode      the code of the origin account
	 * @param destinationAccountCode the code of the destination account
	 * @throws AccountDoesNotExistsException when an informed account is not found
	 *                                       on the database
	 */
	private void validateAccounts(String originAccountCode, String destinationAccountCode)
			throws AccountDoesNotExistsException {
		if (!accountRepository.existsByCode(originAccountCode)) {
			throw new AccountDoesNotExistsException("Conta com código " + originAccountCode + " não existe");
		}

		if (destinationAccountCode != null && !destinationAccountCode.isEmpty()
				&& !accountRepository.existsByCode(destinationAccountCode)) {
			throw new AccountDoesNotExistsException("Conta com código " + destinationAccountCode + " não existe");
		}
	}

	/**
	 * Creates a transfer transaction
	 *
	 * @param transactionTO the transaction to
	 */
	private void createTransferTransaction(TransactionRequestTO transactionTO) {
		List<Transaction> transactionsToSave = new ArrayList<>();
		Transaction debitTransaction = new Transaction();
		debitTransaction.setTypeOperation(TRANSFERENCIA);
		debitTransaction.setValueTransaction(transactionTO.getValue().negate());
		debitTransaction.setDateTime(now());
		accountRepository.findByCode(transactionTO.getOriginAccountCode())
				.ifPresent(account -> debitTransaction.setAccountId(account.getId()));
		transactionsToSave.add(debitTransaction);

		Transaction creditTransaction = new Transaction();
		creditTransaction.setTypeOperation(TRANSFERENCIA);
		creditTransaction.setValueTransaction(transactionTO.getValue());
		creditTransaction.setDateTime(now());
		accountRepository.findByCode(transactionTO.getDestinationAccountCode())
				.ifPresent(account -> creditTransaction.setAccountId(account.getId()));
		transactionsToSave.add(creditTransaction);

		transactionRepository.saveAll(transactionsToSave);
	}

	/**
	 * Creates a transaction in a single account
	 *
	 * @param transactionTO the transaction to
	 */
	private void createSelfTransaction(TransactionRequestTO transactionTO) {
		Transaction transaction = new Transaction();

		if (SAQUE.getDescription().equals(transactionTO.getOperation())) {
			transaction.setTypeOperation(SAQUE);
		} else {
			transaction.setTypeOperation(DEPOSITO);
		}

		transaction.setDateTime(now());
		accountRepository.findByCode(transactionTO.getOriginAccountCode())
				.ifPresent(account -> transaction.setAccountId(account.getId()));

		if (SAQUE.getDescription().equals(transactionTO.getOperation())) {
			transaction.setValueTransaction(transactionTO.getValue().negate());
		} else if (DEPOSITO.getDescription().equals(transactionTO.getOperation())) {
			transaction.setValueTransaction(transactionTO.getValue());
		}

		transactionRepository.save(transaction);
	}

}
