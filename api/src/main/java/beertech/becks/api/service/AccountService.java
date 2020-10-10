package beertech.becks.api.service;

import beertech.becks.api.entities.Account;
import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.tos.request.AccountRequestTO;

public interface AccountService {

	Account createAccount(AccountRequestTO accountTO) throws AccountAlreadyExistsException;
}
