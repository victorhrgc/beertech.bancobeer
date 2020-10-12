package beertech.becks.api.service.impl;

import beertech.becks.api.entities.Transaction;
import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.repositories.TransactionRepository;
import beertech.becks.api.tos.response.BalanceResponseTO;
import org.springframework.beans.factory.annotation.Autowired;

import beertech.becks.api.entities.Account;
import beertech.becks.api.repositories.AccountRepository;
import beertech.becks.api.service.AccountService;
import beertech.becks.api.tos.request.AccountRequestTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public Account createAccount(AccountRequestTO accountRequestTO) throws AccountAlreadyExistsException {
		if (accountRepository.existsByCode(accountRequestTO.getCode())) {
			throw new AccountAlreadyExistsException("Conta já cadastrada");
		}

		Account account = new Account();
		account.setCode(accountRequestTO.getCode());
		return accountRepository.save(account);
	}

	@Override
	public BalanceResponseTO getBalance(String accountCode) throws AccountDoesNotExistsException {
		validateAccounts(accountCode, null);
		BalanceResponseTO balance = new BalanceResponseTO();

		accountRepository.findByCode(accountCode)
				.ifPresent(account -> balance.setBalance(transactionRepository.findByAccountId(account.getId()).stream()
						.map(Transaction::getValueTransaction).reduce(BigDecimal.ZERO, BigDecimal::add)));

		return balance;
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
}
