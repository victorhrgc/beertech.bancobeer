package beertech.becks.api.service;

import static beertech.becks.api.constants.ConstantsTests.Hash.accountCode2;
import static beertech.becks.api.constants.ConstantsTests.Values.VALUE_50;
import static beertech.becks.api.fixture.AccountFixture.aAccountForTestsTransaction;
import static beertech.becks.api.fixture.AccountFixture.aAccountForTransfer;
import static java.math.BigDecimal.ZERO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import beertech.becks.api.entities.PaymentSlip;
import beertech.becks.api.model.PaymentCategory;
import beertech.becks.api.model.TypeOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import beertech.becks.api.entities.Account;
import beertech.becks.api.entities.Transaction;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.account.AccountDoesNotHaveEnoughBalanceException;
import beertech.becks.api.repositories.AccountRepository;
import beertech.becks.api.repositories.TransactionRepository;
import beertech.becks.api.service.impl.TransactionServiceImpl;
import beertech.becks.api.tos.request.TransferRequestTO;
import beertech.becks.api.tos.response.StatementResponseTO;

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
            when(transactionRepositoryMock.save(any())).thenReturn(new Transaction());
            when(accountRepositoryMock.save(any())).thenReturn(currentAccount);

            Account accountDeposit = transactionServiceMock.createDeposit(currentAccount.getCode(), VALUE_50);
            assertNotNull(accountDeposit);
            assertEquals(currentAccount.getBalance(), accountDeposit.getBalance());
        }

        @Test
        public void shouldPostTransactionWithdrawal() throws AccountDoesNotExistsException, AccountDoesNotHaveEnoughBalanceException {

            when(accountRepositoryMock.findByCode(currentAccount.getCode())).thenReturn(Optional.of(currentAccount));
            when(transactionRepositoryMock.save(any())).thenReturn(new Transaction());
            when(accountRepositoryMock.save(currentAccount)).thenReturn(currentAccount);

            Account accountResponse = transactionServiceMock.createWithdrawal(currentAccount.getCode(), BigDecimal.TEN);
            assertNotNull(accountResponse);
            assertEquals(new BigDecimal(40), accountResponse.getBalance());
        }

        @Test
        public void shouldPostTransactionTransfer() throws AccountDoesNotExistsException, AccountDoesNotHaveEnoughBalanceException {

            Account destinationAccount = aAccountForTransfer();

            TransferRequestTO transferRequestTO = TransferRequestTO.builder()
                    .destinationAccountCode(accountCode2)
                    .value(VALUE_50).build();

            when(accountRepositoryMock.findByCode(currentAccount.getCode())).thenReturn(Optional.of(currentAccount));
            when(accountRepositoryMock.findByCode(transferRequestTO.getDestinationAccountCode())).thenReturn(Optional.of(destinationAccount));
            when(transactionRepositoryMock.save(any())).thenReturn(new Transaction());
            when(accountRepositoryMock.save(currentAccount)).thenReturn(currentAccount);
            when(accountRepositoryMock.save(destinationAccount)).thenReturn(destinationAccount);

            Account accountTransfer = transactionServiceMock.createTransfer(currentAccount.getCode(), transferRequestTO, TypeOperation.TRANSFERENCIA, PaymentCategory.OTHERS);
            assertNotNull(accountTransfer);
            assertEquals(ZERO, accountTransfer.getBalance());
        }

        @Test
        public void shouldPostTransactionPayment() throws AccountDoesNotExistsException, AccountDoesNotHaveEnoughBalanceException {

            PaymentSlip paymentSlip = PaymentSlip.builder().
                    code("20201025-005000-EN-001/01234-001/43210").
                    originAccountCode("01234").
                    destinationAccountCode("43210").
                    value(VALUE_50).
                    category(PaymentCategory.ENTERTAINMENT).
                    build();

            Account destinationAccount = new Account();
            destinationAccount.setBalance(ZERO);

            when(accountRepositoryMock.findByCode(paymentSlip.getOriginAccountCode())).thenReturn(Optional.of(currentAccount));
            when(accountRepositoryMock.findByCode(paymentSlip.getDestinationAccountCode())).thenReturn(Optional.of(destinationAccount));
            when(transactionRepositoryMock.save(any())).thenReturn(new Transaction());
            when(accountRepositoryMock.save(currentAccount)).thenReturn(currentAccount);
            when(accountRepositoryMock.save(destinationAccount)).thenReturn(destinationAccount);

            Account accountResponse = transactionServiceMock.createPayment(paymentSlip);
            assertNotNull(accountResponse);
            assertEquals(ZERO, accountResponse.getBalance());
        }

        @Test
        public void shouldPostTransactionPaymentToExternalBank() throws AccountDoesNotExistsException, AccountDoesNotHaveEnoughBalanceException {

            PaymentSlip paymentSlip = PaymentSlip.builder().
                    code("20201025-005000-EN-001/01234-002/43210").
                    originAccountCode("01234").
                    destinationAccountCode("43210").
                    value(VALUE_50).
                    category(PaymentCategory.ENTERTAINMENT).
                    build();


            when(accountRepositoryMock.findByCode(paymentSlip.getOriginAccountCode())).thenReturn(Optional.of(currentAccount));
            when(transactionRepositoryMock.save(any())).thenReturn(new Transaction());
            when(accountRepositoryMock.save(currentAccount)).thenReturn(currentAccount);

            Account accountResponse = transactionServiceMock.createPaymentToExternalBank(paymentSlip);
            assertNotNull(accountResponse);
            assertEquals(ZERO, accountResponse.getBalance());
        }
    }

}

