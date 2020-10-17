package beertech.becks.api.service;

import beertech.becks.api.entities.Account;
import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.user.UserDoesNotExistException;
import beertech.becks.api.tos.request.AccountRequestTO;
import beertech.becks.api.tos.response.BalanceResponseTO;

import java.util.List;

public interface AccountService {

	Account createAccount(AccountRequestTO accountTO) throws AccountAlreadyExistsException, UserDoesNotExistException;

	BalanceResponseTO getBalance(String accountCode) throws AccountDoesNotExistsException;

	List<Account> getAll();

	Account getAccountByCode(String accountCode) throws AccountDoesNotExistsException;

	Account getAccountById(Long accountId) throws AccountDoesNotExistsException;
}
