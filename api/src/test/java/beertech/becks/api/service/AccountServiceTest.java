package beertech.becks.api.service;

import beertech.becks.api.entities.Account;
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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.anyString;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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

    @Test
    public void shouldListAllAccounts() {
        givenAccountRepositoryFindAll();
        List<Account> accountList = whenCallGetAllAccounts();
        thenExpectNoException();
        thenExpectListAccounts(accountList);
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

    public void givenAccountRepositoryFindAll() {
	    List<Account> accountList = Arrays.asList(new Account(0L, "1", 1L), new Account(1L, "2", 2L));
        when(accountRepository.findAll()).thenReturn(accountList);
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

    private List<Account> whenCallGetAllAccounts() {
        try {
            return accountService.getAll();
        } catch (Exception e) {
            exception = e;
        }
        return null;
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

    private void thenExpectListAccounts(List<Account> listAccount) {
        assertThat(listAccount).isNotNull();
        assertEquals(listAccount.size(), 2);
    }

}
