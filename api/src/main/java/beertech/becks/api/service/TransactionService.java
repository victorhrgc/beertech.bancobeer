package beertech.becks.api.service;

import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.transaction.InvalidTransactionOperationException;
import beertech.becks.api.tos.request.TransactionRequestTO;
import beertech.becks.api.tos.response.AccountStatementResponseTO;

public interface TransactionService {

	void createTransaction(TransactionRequestTO transactionTO) throws AccountDoesNotExistsException, InvalidTransactionOperationException;

	AccountStatementResponseTO getStatements(TransactionRequestTO transactionTO) throws AccountDoesNotExistsException, InvalidTransactionOperationException;
}
