package beertech.becks.api.service.impl;

import beertech.becks.api.entities.Account;
import beertech.becks.api.entities.Transaction;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.account.AccountDoesNotHaveEnoughBalanceException;
import beertech.becks.api.model.TypeOperation;
import beertech.becks.api.repositories.AccountRepository;
import beertech.becks.api.repositories.TransactionRepository;
import beertech.becks.api.service.AccountService;
import beertech.becks.api.service.TransactionService;
import beertech.becks.api.tos.request.TransferRequestTO;
import beertech.becks.api.tos.response.StatementResponseTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static beertech.becks.api.model.TypeOperation.*;
import static beertech.becks.api.utils.BigDecimalUtil.*;
import static java.time.LocalDateTime.now;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountRepository accountRepository,
                                  AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @Override
    public StatementResponseTO getStatements(String accountCode) throws AccountDoesNotExistsException {
        Account account = findAccountByCode(accountCode);
        return StatementResponseTO.builder()
                .accountStatements(getListTranscationByAccountId(account))
                .balance(account.getBalance())
                .build();
    }

    @Override
    public Account createDeposit(String accountCode, BigDecimal value) throws AccountDoesNotExistsException {
        Account accountByCode = findAccountByCode(accountCode);
        saveTransaction(value, accountByCode, now(), DEPOSITO);
        return updateAccountAfterDeposit(accountByCode, value);
    }

    @Override
    public Account createWithdrawal(String accountCode, BigDecimal value) throws AccountDoesNotExistsException, AccountDoesNotHaveEnoughBalanceException {
        Account accountByCode = findAccountByCode(accountCode);
        accountService.checkAvailableBalance(accountByCode.getBalance(), negateValue(value));
        saveTransaction(negateValue(value), accountByCode, now(), SAQUE);
        return updateAccountAfterWithDraw(accountByCode, negateValue(value));
    }

    @Override
    public Account createTransfer(String accountCode, TransferRequestTO transferRequestTO) throws AccountDoesNotExistsException, AccountDoesNotHaveEnoughBalanceException {

        LocalDateTime currentDate = now();

        Account originAccount = findAccountByCode(accountCode);
        Account destinationAccount = findAccountByCode(transferRequestTO.getDestinationAccountCode());

        accountService.checkAvailableBalance(originAccount.getBalance(), negateValue(transferRequestTO.getValue()));
        saveTransactionTransferDebit(transferRequestTO, currentDate, originAccount);
        saveTransactionTransferCredit(transferRequestTO, currentDate, destinationAccount);

        updateAccountAfterDeposit(destinationAccount, transferRequestTO.getValue());
        return updateAccountAfterWithDraw(originAccount, negateValue(transferRequestTO.getValue()));

    }

    private void saveTransactionTransferDebit(TransferRequestTO transferRequestTO, LocalDateTime currentDate, Account account) {
        saveTransaction(negateValue(transferRequestTO.getValue()), account, currentDate, TRANSFERENCIA);
    }

    private void saveTransactionTransferCredit(TransferRequestTO transferRequestTO, LocalDateTime currentDate, Account account) {
        saveTransaction(transferRequestTO.getValue(), account, currentDate, TRANSFERENCIA);
    }

    private void saveTransaction(BigDecimal value, Account accountByCode, LocalDateTime currentDate, TypeOperation typeOperation) {
        transactionRepository.save(Transaction.builder()
                .typeOperation(typeOperation)
                .dateTime(currentDate)
                .valueTransaction(value)
                .accountId(accountByCode.getId())
                .build());
    }

    private List<Transaction> getListTranscationByAccountId(Account account) {
        return transactionRepository.findByAccountId(account.getId());
    }

    private Account findAccountByCode(String accountCode) throws AccountDoesNotExistsException {
        return accountRepository.findByCode(accountCode).orElseThrow(AccountDoesNotExistsException::new);
    }

    private Account updateAccountAfterDeposit(Account account, BigDecimal value) {
        account.setBalance(sumTwoValues(account.getBalance(), value));
        return accountRepository.save(account);
    }

    private Account updateAccountAfterWithDraw(Account account, BigDecimal value) {
        account.setBalance(sumTwoValues(account.getBalance(), value));
        return accountRepository.save(account);
    }

}
