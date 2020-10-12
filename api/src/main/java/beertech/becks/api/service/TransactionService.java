package beertech.becks.api.service;

import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.tos.request.TransactionRequestTO;

public interface TransactionService {

	void createTransaction(TransactionRequestTO transactionTO) throws AccountDoesNotExistsException;

}
