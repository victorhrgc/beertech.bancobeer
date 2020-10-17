package beertech.becks.api.service;

import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.transaction.InvalidTransactionOperationException;
import beertech.becks.api.tos.request.StatementRequestTO;
import beertech.becks.api.tos.request.TransactionRequestTO;
import beertech.becks.api.tos.request.TransferRequestTO;
import beertech.becks.api.tos.response.StatementResponseTO;

import java.math.BigDecimal;

public interface TransactionService {

	void createTransaction(TransactionRequestTO transactionTO) throws AccountDoesNotExistsException, InvalidTransactionOperationException;

	void createDeposit(String accountCode, BigDecimal value) throws AccountDoesNotExistsException;

	void createWithdrawal(String accountCode, BigDecimal value) throws AccountDoesNotExistsException;

	void createTransfer(String accountCode, TransferRequestTO transferRequestTO) throws AccountDoesNotExistsException;

	StatementResponseTO getStatements(String accountCode, StatementRequestTO statementRequestTO) throws AccountDoesNotExistsException, InvalidTransactionOperationException;
}
