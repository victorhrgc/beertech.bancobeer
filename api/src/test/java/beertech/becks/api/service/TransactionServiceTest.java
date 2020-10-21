package beertech.becks.api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

public class TransactionServiceTest {

    @RunWith(Suite.class)
    @Suite.SuiteClasses({
            TransactionServiceTest.WhenRunningGetStatements.class
    })
    public class AllTests {
    }


    public static class WhenRunningGetStatements extends TransactionServiceTest {

        @Test
        public void givenAccountCode() {

        }



    }




}
