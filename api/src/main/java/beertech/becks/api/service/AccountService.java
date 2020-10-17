package beertech.becks.api.service;

import beertech.becks.api.entities.Account;
import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.tos.request.AccountRequestTO;
import beertech.becks.api.tos.response.BalanceResponseTO;

import java.util.List;

public interface AccountService {

	Account createAccount(AccountRequestTO accountTO) throws AccountAlreadyExistsException;

	List<Account> getAll();

	BalanceResponseTO getBalance(String accountCode) throws AccountDoesNotExistsException;
}
