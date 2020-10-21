package beertech.becks.api.service.impl;

import beertech.becks.api.entities.Account;
import beertech.becks.api.entities.Transaction;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.model.TypeOperation;
import beertech.becks.api.repositories.AccountRepository;
import beertech.becks.api.repositories.TransactionRepository;
import beertech.becks.api.service.TransactionService;
import beertech.becks.api.tos.request.TransferRequestTO;
import beertech.becks.api.tos.response.StatementResponseTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static beertech.becks.api.model.TypeOperation.*;
import static beertech.becks.api.utils.BigDecimalUtil.*;
import static java.time.LocalDateTime.now;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

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
    public Account createWithdrawal(String accountCode, BigDecimal value) throws AccountDoesNotExistsException {
        Account accountByCode = findAccountByCode(accountCode);
        saveTransaction(value, accountByCode, now(), SAQUE);
        return updateAccountAfterWithDraw(accountByCode, value);
    }

    @Override
    public Account createTransfer(String accountCode, TransferRequestTO transferRequestTO) throws AccountDoesNotExistsException {

        LocalDateTime currentDate = now();

        Account originAccount = findAccountByCode(accountCode);
        Account destinationAccount = findAccountByCode(transferRequestTO.getDestinationAccountCode());

        saveTransactionTransferDebit(transferRequestTO, currentDate, originAccount);
        saveTranscationTransferCredit(transferRequestTO, currentDate, destinationAccount);

        updateAccountAfterDeposit(destinationAccount, transferRequestTO.getValue());
        return updateAccountAfterWithDraw(originAccount, transferRequestTO.getValue());

    }

    private void saveTransactionTransferDebit(TransferRequestTO transferRequestTO, LocalDateTime currentDate, Account account) {
        saveTransaction(negateValue(transferRequestTO.getValue()), account, currentDate, TRANSFERENCIA);
    }

    private void saveTranscationTransferCredit(TransferRequestTO transferRequestTO, LocalDateTime currentDate, Account account) {
        saveTransaction(transferRequestTO.getValue(), account, currentDate, TRANSFERENCIA);
    }

    private Transaction saveTransaction(BigDecimal value, Account accountByCode, LocalDateTime currentDate, TypeOperation typeOperation) {
        return transactionRepository.save(Transaction.builder()
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
        account.setBalance(subtractTwoValues(account.getBalance(), value));
        return accountRepository.save(account);
    }

}
