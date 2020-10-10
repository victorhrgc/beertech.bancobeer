package beertech.becks.api.service.impl;

import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;

import beertech.becks.api.entities.Account;
import beertech.becks.api.repositories.AccountRepository;
import beertech.becks.api.service.AccountService;
import beertech.becks.api.tos.request.AccountRequestTO;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository repository;

	@Override
	public Account createAccount(AccountRequestTO accountRequestTO) throws AccountAlreadyExistsException {
		if (repository.existsByCode(accountRequestTO.getCode())) {
			throw new AccountAlreadyExistsException("Conta já cadastrada");
		}

		Account account = new Account();
		account.setCode(accountRequestTO.getCode());
		return repository.save(account);
	}
}
