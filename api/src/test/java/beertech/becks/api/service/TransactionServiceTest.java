package beertech.becks.api.service;

import beertech.becks.api.entities.Account;
import beertech.becks.api.entities.Transaction;
import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.account.AccountDoesNotHaveEnoughBalanceException;
import beertech.becks.api.exception.transaction.InvalidTransactionOperationException;
import beertech.becks.api.fixture.TransactionFixture;
import beertech.becks.api.repositories.AccountRepository;
import beertech.becks.api.repositories.TransactionRepository;
import beertech.becks.api.service.impl.TransactionServiceImpl;
import beertech.becks.api.tos.request.TransactionRequestTO;
import beertech.becks.api.tos.request.TransferRequestTO;
import beertech.becks.api.tos.response.StatementResponseTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static beertech.becks.api.constants.ConstantsTests.Hash.HASH_eccbc87e4b5ce2fe28308fd9f2a7baf3;
import static beertech.becks.api.constants.ConstantsTests.Values.VALUE_100;
import static beertech.becks.api.constants.ConstantsTests.Values.VALUE_50;
import static beertech.becks.api.fixture.AccountFixture.*;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    protected Account currentAccount;
    protected Transaction transaction;

    @Mock
    protected TransactionService transactionServiceMock;
    @Mock
    protected AccountService accountServiceMock;

    @Mock
    protected TransactionRepository transactionRepositoryMock;
    @Mock
    protected AccountRepository accountRepositoryMock;

    protected List<StatementResponseTO> statementResponseTOList = new ArrayList<>();

    @RunWith(Suite.class)
    @Suite.SuiteClasses({
            TransactionServiceTest.GetStatementsSucessfully.class,
            TransactionServiceTest.GetStatementUnsuccessfully.class,
            TransactionServiceTest.PostTransactionSuccessfully.class
    })
    public class AllTests {
    }

    @Before
    public void buildContext() {
        MockitoAnnotations.initMocks(this);
    }

    public static class GetStatementsSucessfully extends TransactionServiceTest {

        @Override
        public void buildContext() {
            super.buildContext();
            currentAccount = aAccountForTestsTransaction();
            transactionServiceMock = new TransactionServiceImpl(transactionRepositoryMock, accountRepositoryMock, accountServiceMock);
        }

        @Test
        public void shouldListAllStatementsResponseTO() throws Exception {
            when(accountRepositoryMock.findByCode(any())).thenReturn(Optional.of(currentAccount));
            StatementResponseTO statementResponseTO = transactionServiceMock.getStatements(currentAccount.getCode());
            assertNotNull(statementResponseTO);
        }

    }

    public static class GetStatementUnsuccessfully extends TransactionServiceTest {

        @Override
        public void buildContext() {
            super.buildContext();
            currentAccount = new Account();
            transactionServiceMock = new TransactionServiceImpl(transactionRepositoryMock, accountRepositoryMock, accountServiceMock);
        }

        @Test(expected = AccountDoesNotExistsException.class)
        public void shouldShowValidationErrorOnInvalidAccount() throws Exception {
            transactionServiceMock.getStatements(currentAccount.getCode());
        }

    }

    public static class PostTransactionSuccessfully extends TransactionServiceTest {

        @Override
        public void buildContext() {
            super.buildContext();
            currentAccount = aAccountForTestsTransaction();
            transactionServiceMock = new TransactionServiceImpl(transactionRepositoryMock, accountRepositoryMock, accountServiceMock);
        }

        @Test
        public void shouldPostTransactionDeposit() throws AccountDoesNotExistsException {
            when(accountRepositoryMock.findByCode(any())).thenReturn(Optional.of(currentAccount));
            when(transactionRepositoryMock.save(any()));
            when(accountRepositoryMock.save(any())).thenReturn(currentAccount);

            Account accountDeposit = transactionServiceMock.createDeposit(currentAccount.getCode(), VALUE_50);
            assertNotNull(accountDeposit);
            assertEquals(currentAccount.getBalance(), accountDeposit.getBalance());
        }

        @Test
        public void shouldPostTransactionWithdrawal() throws AccountDoesNotExistsException, AccountDoesNotHaveEnoughBalanceException {

            Account destinationAccount = aAccountForTransfer();

            TransferRequestTO transferRequestTO = TransferRequestTO.builder()
                    .destinationAccountCode(HASH_eccbc87e4b5ce2fe28308fd9f2a7baf3)
                    .value(VALUE_50).build();

            when(accountRepositoryMock.findByCode(any())).thenReturn(Optional.of(currentAccount));
            when(accountRepositoryMock.findByCode(any())).thenReturn(Optional.of(destinationAccount));
            when(transactionRepositoryMock.save(any()));
            when(accountRepositoryMock.save(any())).thenReturn(currentAccount);

            Account accountTransfer = transactionServiceMock.createTransfer(currentAccount.getCode(), transferRequestTO);
            assertNotNull(accountTransfer);
            assertEquals(currentAccount.getBalance(), ZERO);
        }

        @Test
        public void shouldPostTransactionTransfer() {

        }

    }

}

