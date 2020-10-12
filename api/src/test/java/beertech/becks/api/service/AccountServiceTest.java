package beertech.becks.api.service;

import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.repositories.AccountRepository;
import beertech.becks.api.service.impl.AccountServiceImpl;
import beertech.becks.api.tos.request.AccountRequestTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.anyString;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

	@InjectMocks
	private AccountServiceImpl accountService;

	@Mock
    private AccountRepository accountRepository;

    private AccountRequestTO accountRequestTO;


    private Exception exception;

    // Tests

	@Test
    public void shouldCreateAccountFailWhenAccountAlreadyExists() {
        givenValidAccountRequestTO();
        givenAccountRepositoryExistsByCodeReturnsTrue();
        whenCallAccountServiceCreateAccount();
        thenExpectAccountAlreadyExistsException();
    }

    @Test
    public void shouldCreateAccountSuccessfully() {
        givenValidAccountRequestTO();
        givenAccountRepositoryExistsByCodeReturnsFalse();
        whenCallAccountServiceCreateAccount();
        thenExpectNoException();
    }

    @Test
    public void shouldGetBalanceFailWhenAccountDoesNotExist() {
        givenValidAccountRequestTO();
        givenAccountRepositoryExistsByCodeReturnsFalse();
        whenCallAccountServiceGetBalance();
        thenExpectAccountDoesNotExistsException();
    }

    @Test
    public void shouldGetBalanceSuccessfully() {
        givenValidAccountRequestTO();
        givenAccountRepositoryExistsByCodeReturnsTrue();
        whenCallAccountServiceGetBalance();
        thenExpectNoException();
    }



    // Givens

    private void givenValidAccountRequestTO() {
        accountRequestTO = new AccountRequestTO();
        accountRequestTO.setCode(UUID.randomUUID().toString());
    }

	public void givenAccountRepositoryExistsByCodeReturnsTrue() {
		doReturn(true).when(accountRepository).existsByCode(anyString());
	}

	public void givenAccountRepositoryExistsByCodeReturnsFalse() {
		doReturn(false).when(accountRepository).existsByCode(anyString());
	}

	// Whens
	private void whenCallAccountServiceCreateAccount() {
		try {
			accountService.createAccount(accountRequestTO);
		} catch (Exception e) {
			exception = e;
		}
	}

    private void whenCallAccountServiceGetBalance() {
        try {
            accountService.getBalance(accountRequestTO.getCode());
        } catch (Exception e) {
            exception = e;
        }
    }

	// Thens

	private void thenExpectAccountAlreadyExistsException() {
	    assertThat(exception).isInstanceOf(AccountAlreadyExistsException.class);
    }

    private void thenExpectNoException() {
        assertThat(exception).isNull();
    }

    private void thenExpectAccountDoesNotExistsException() {
        assertThat(exception).isInstanceOf(AccountDoesNotExistsException.class);
    }


}
