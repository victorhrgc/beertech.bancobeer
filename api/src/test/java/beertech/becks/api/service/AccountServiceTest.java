package beertech.becks.api.service;

import beertech.becks.api.entities.Account;
import beertech.becks.api.entities.User;
import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.account.AccountDoesNotHaveEnoughBalanceException;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

	private String accountCode = "12345";
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
		whenCallAccountServiceGetAccountByCode(accountCode);
		thenExpectAccountDoesNotExistsException();
	}

	@Test
	public void shouldGetAccountByIdFailWhenAccountDoesNotExist() {
		whenCallAccountServiceGetAccountById(accountId);
		thenExpectAccountDoesNotExistsException();
	}

	@Test
	public void given_unavailableBalance_when_checkAvailableBalance_then_should_return_exception() {

		// GIVEN
		BigDecimal currentBalance = BigDecimal.ZERO;
		BigDecimal transactionValue = BigDecimal.TEN;

		// WHEN/THEN
		assertThrows(AccountDoesNotHaveEnoughBalanceException.class, () -> {
			accountService.checkAvailableBalance(currentBalance, transactionValue);
		});

	}

	@Test
	public void given_accountCodeSizeBiggerThenFive_when_createAccount_then_should_adjustSize() throws AccountAlreadyExistsException, UserDoesNotExistException {

		// GIVEN
		AccountRequestTO accountRequestTO = new AccountRequestTO();
		accountRequestTO.setCode("0123456789");
		accountRequestTO.setUserId(10L);

		String compareCode = accountRequestTO.getCode().substring(0, 5);

		givenAccountRepositoryExistsByCodeReturnsFalse(compareCode);
		givenUserRepositoryExistsByIdReturnsTrue(accountRequestTO.getUserId());
		when(userRepository.getOne(accountRequestTO.getUserId())).thenReturn(new User());

		Account account = Account.builder().code(compareCode).build();
		when(accountRepository.save(any())).thenReturn(account);

		// WHEN
		Account accountResponse = accountService.createAccount(accountRequestTO);

		// THEN
		assertTrue(accountResponse.getCode().length() == 5);
		assertEquals(compareCode, accountResponse.getCode());
	}

	@Test
	public void given_accountCodeSizeLowerThenFive_when_createAccount_then_should_adjustSize() throws AccountAlreadyExistsException, UserDoesNotExistException {

		// GIVEN
		AccountRequestTO accountRequestTO = new AccountRequestTO();
		accountRequestTO.setCode("012");
		accountRequestTO.setUserId(10L);

		givenAccountRepositoryExistsByCodeReturnsFalse("00012");
		givenUserRepositoryExistsByIdReturnsTrue(accountRequestTO.getUserId());
		when(userRepository.getOne(accountRequestTO.getUserId())).thenReturn(new User());

		Account account = Account.builder().code("00012").build();
		when(accountRepository.save(any())).thenReturn(account);

		// WHEN
		Account accountResponse = accountService.createAccount(accountRequestTO);

		// THEN
		assertTrue(accountResponse.getCode().length() == 5);
		assertEquals("00012", accountResponse.getCode());
	}

	// Givens

	private void givenValidAccountRequestTO() {
		accountRequestTO = new AccountRequestTO();
		accountRequestTO.setCode(accountCode);
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
