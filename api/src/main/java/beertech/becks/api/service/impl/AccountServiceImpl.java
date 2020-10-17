package beertech.becks.api.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import beertech.becks.api.entities.Account;
import beertech.becks.api.entities.Transaction;
import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.repositories.AccountRepository;
import beertech.becks.api.repositories.TransactionRepository;
import beertech.becks.api.service.AccountService;
import beertech.becks.api.tos.request.AccountRequestTO;
import beertech.becks.api.tos.response.BalanceResponseTO;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public Account createAccount(AccountRequestTO accountRequestTO) throws AccountAlreadyExistsException {
		if (accountRepository.existsByCode(accountRequestTO.getCode())) {
			throw new AccountAlreadyExistsException(accountRequestTO.getCode());
		}

		Account account = new Account();
		account.setCode(accountRequestTO.getCode());
		account.setUserId(accountRequestTO.getUserId());
		return accountRepository.save(account);
	}

	@Override
	public BalanceResponseTO getBalance(String accountCode) throws AccountDoesNotExistsException {
		if (!accountRepository.existsByCode(accountCode)) {
			throw new AccountDoesNotExistsException();
		}

		BalanceResponseTO balance = new BalanceResponseTO();

		accountRepository.findByCode(accountCode)
				.ifPresent(account -> balance.setBalance(transactionRepository.findByAccountId(account.getId()).stream()
						.map(Transaction::getValueTransaction).reduce(BigDecimal.ZERO, BigDecimal::add)));

		return balance;
	}

	@Override
	public List<Account> getAll() {
		return accountRepository.findAll();
	}

	@Override
	public Account getAccountByCode(String accountCode) throws AccountDoesNotExistsException {
		return accountRepository.findByCode(accountCode)
				.orElseThrow(AccountDoesNotExistsException::new);
	}

	@Override
	public Account getAccountById(Long accountId) throws AccountDoesNotExistsException {
		return accountRepository.findById(accountId)
				.orElseThrow(AccountDoesNotExistsException::new);
	}
}
