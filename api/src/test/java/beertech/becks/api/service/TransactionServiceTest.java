package beertech.becks.api.service;

import beertech.becks.api.entities.Account;
import beertech.becks.api.repositories.AccountRepository;
import beertech.becks.api.repositories.TransactionRepository;
import beertech.becks.api.tos.response.StatementResponseTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static beertech.becks.api.fixture.AccountFixture.aAccountForTestsTransaction;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    protected Account currentAccount;

    @Mock protected TransactionService transactionServiceMock;
    @Mock protected AccountService AccountServiceMock;

    @Mock protected TransactionRepository transactionRepositoryMock;
    @Mock protected AccountRepository accountRepositoryMock;

    protected List<StatementResponseTO> statementResponseTOList = new ArrayList<>();

    @RunWith(Suite.class)
    @Suite.SuiteClasses({
            TransactionServiceTest.GivenAccountCurrentForGetStatements.class
    })
    public class AllTests {
    }

    @Before
    public void buildContext() {
        MockitoAnnotations.initMocks(this);
        currentAccount = aAccountForTestsTransaction();
    }

    public static class GivenAccountCurrentForGetStatements extends TransactionServiceTest {

        @Override
        public void buildContext() {
            super.buildContext();
        }

        @Test
        public void shouldListAllStatementsResponseTO() {
            when(accountRepositoryMock.findByCode(any())).thenReturn(Optional.of(currentAccount));
        }


    }




}
