package beertech.becks.api.service;

import beertech.becks.api.entities.Account;
import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.user.UserDoesNotExistException;
import beertech.becks.api.repositories.AccountRepository;
import beertech.becks.api.repositories.UserRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

	@InjectMocks
	private AccountServiceImpl accountService;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private UserRepository userRepository;

	private AccountRequestTO accountRequestTO;

	private String accountCode = "accountCode";
	private Long accountId = 1L;
	private Long userId = 1L;

	private Exception exception;

	private List<Account> allAccounts;

	// Tests

	@Test
	public void shouldCreateAccountSuccessfully() {
		givenValidAccountRequestTO();
		givenAccountRepositoryExistsByCodeReturnsFalse(accountRequestTO.getCode());
		givenUserRepositoryExistsByIdReturnsTrue(accountRequestTO.getUserId());
		whenCallAccountServiceCreateAccount();
		thenExpectNoException();
	}

	@Test
	public void shouldCreateAccountFailWhenAccountAlreadyExists() {
		givenValidAccountRequestTO();
		givenAccountRepositoryExistsByCodeReturnsTrue();
		whenCallAccountServiceCreateAccount();
		thenExpectAccountAlreadyExistsException();
	}

	@Test
	public void shouldCreateAccountFailWhenUserDoesNotExist() {
		givenValidAccountRequestTO();
		givenUserRepositoryExistsByIdReturnsFalse(accountRequestTO.getUserId());
		whenCallAccountServiceCreateAccount();
		thenExpectUserDoesNotExistException();
	}

	@Test
	public void shouldGetBalanceSuccessfully() {
		givenAccountRepositoryExistsByCodeReturnsTrue();
		whenCallAccountServiceGetBalance();
		thenExpectNoException();
	}

	@Test
	public void shouldGetBalanceFailWhenAccountDoesNotExist() {
		givenAccountRepositoryExistsByCodeReturnsFalse(accountCode);
		whenCallAccountServiceGetBalanceForSpecificAccount(accountCode);
		thenExpectAccountDoesNotExistsException();
	}

	@Test
	public void shouldGetAllAccountsSuccessfully() {
		givenAccountRepositoryFindAllReturnsValues();
		whenCallGetAllAccounts();
		thenExpectNoException();
		thenExpectListAccounts();
	}

	@Test
	public void shouldGetAllAccountsByUserIdSuccessfully() {
		givenUserRepositoryExistsByIdReturnsTrue(userId);
		givenAccountRepositoryFindByUserIdReturnsValues(userId);
		whenCallGetAllAccountsByUserId();
		thenExpectNoException();
		thenExpectListAccounts();
	}

	@Test
	public void shouldGetAllAccountsByUserIdFailWhenUserDoesNotExist() {
		givenUserRepositoryExistsByIdReturnsFalse(userId);
		whenCallGetAllAccountsByUserId();
		thenExpectUserDoesNotExistException();
	}

	@Test
	public void shouldGetAccountByCodeFailWhenAccountDoesNotExist() {
		// givenAccountRepositoryExistsByCodeReturnsFalse(accountCode);
		whenCallAccountServiceGetAccountByCode(accountCode);
		thenExpectAccountDoesNotExistsException();
	}

	@Test
	public void shouldGetAccountByIdFailWhenAccountDoesNotExist() {
		// givenAccountRepositoryExistsByCodeReturnsFalse(accountCode);
		whenCallAccountServiceGetAccountById(accountId);
		thenExpectAccountDoesNotExistsException();
	}

	// TODO checkAvailableBalance tests and both of the above success cases
	// TODO check why the above 2 dont work

	// Givens

	private void givenValidAccountRequestTO() {
		accountRequestTO = new AccountRequestTO();
		accountRequestTO.setCode(UUID.randomUUID().toString());
		accountRequestTO.setUserId(UUID.randomUUID().getLeastSignificantBits());
	}

	public void givenAccountRepositoryExistsByCodeReturnsTrue() {
		doReturn(true).when(accountRepository).existsByCode(anyString());
	}

	public void givenUserRepositoryExistsByIdReturnsFalse(Long userId) {
		doReturn(false).when(userRepository).existsById(userId);
	}

	public void givenUserRepositoryExistsByIdReturnsTrue(Long userId) {
		doReturn(true).when(userRepository).existsById(userId);
	}

	public void givenAccountRepositoryExistsByCodeReturnsFalse(String accountCode) {
		when(accountRepository.existsByCode(accountCode)).thenReturn(false);
	}

	public void givenAccountRepositoryFindAllReturnsValues() {
		doReturn(Arrays.asList(new Account(), new Account())).when(accountRepository).findAll();
	}

	public void givenAccountRepositoryFindByUserIdReturnsValues(Long userId) {
		doReturn(Arrays.asList(new Account(), new Account())).when(accountRepository).findByUserId(userId);
	}

	// Whens
	private void whenCallAccountServiceCreateAccount() {
		try {
			accountService.createAccount(accountRequestTO);
		} catch (Exception e) {
			exception = e;
		}
	}

	private void whenCallGetAllAccountsByUserId() {
		try {
			allAccounts = accountService.getAllAccountsByUserId(userId);
		} catch (Exception e) {
			exception = e;
		}
	}

	private void whenCallAccountServiceGetBalanceForSpecificAccount(String accountCode) {
		try {
			accountService.getBalance(accountCode);
		} catch (Exception e) {
			exception = e;
		}
	}

	private void whenCallAccountServiceGetBalance() {
		try {
			accountService.getBalance(anyString());
		} catch (Exception e) {
			exception = e;
		}
	}

	private void whenCallGetAllAccounts() {
		try {
			allAccounts = accountService.getAll();
		} catch (Exception e) {
			exception = e;
		}
	}

	private void whenCallAccountServiceGetAccountByCode(String accountCode) {
		try {
			accountService.getAccountByCode(accountCode);
		} catch (Exception e) {
			exception = e;
		}
	}

	private void whenCallAccountServiceGetAccountById(Long accountId) {
		try {
			accountService.getAccountById(accountId);
		} catch (Exception e) {
			exception = e;
		}
	}

	// Thens

	private void thenExpectAccountAlreadyExistsException() {
		assertThat(exception).isInstanceOf(AccountAlreadyExistsException.class);
	}

	private void thenExpectUserDoesNotExistException() {
		assertThat(exception).isInstanceOf(UserDoesNotExistException.class);
	}

	private void thenExpectNoException() {
		assertThat(exception).isNull();
	}

	private void thenExpectAccountDoesNotExistsException() {
		assertThat(exception).isInstanceOf(AccountDoesNotExistsException.class);
	}

	private void thenExpectListAccounts() {
		assertThat(allAccounts).isNotNull();
		assertEquals(allAccounts.size(), 2);
	}

}
