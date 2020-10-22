package beertech.becks.api.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TransactionServiceTest.AllTests.class,
        AccountServiceTest.AllTests.class
})
public class ServicePackagesTests {
}
