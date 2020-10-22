package beertech.becks.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beertech.becks.api.controllers.AccountController;
import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.user.UserDoesNotExistException;
import beertech.becks.api.service.AccountService;
import beertech.becks.api.tos.request.AccountRequestTO;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
	private static final String ACCOUNTS_ENDPOINT = "/accounts";

	@InjectMocks
	private AccountController accountController;

	@Mock
	private AccountService accountService;
	private MockMvc mockMvc;

	private String accountCode;
	private Long accountId;
	private Long userId;
	private AccountRequestTO accountRequestTO;
	private MockHttpServletResponse response;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
	}

	private String objectToJson(Object o) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(o);
	}

	// Tests
	@Test
	public void createAccountSuccessfullyWithValidAccountRequestTO() throws Exception {
		givenValidAccountRequestTO();
		whenCallPostCreateAccount();
		thenExpectCreatedStatus();
		thenExpectAccountServiceCreateAccountCall();
	}

	@Test
	public void createAccountUnsuccessfullyWithInvalidAccountRequestTO() throws Exception {
		givenInvalidAccountRequestTO();
		whenCallPostCreateAccount();
		thenExpectBadRequestStatus();
		thenExpectAccountServiceCreateAccountNoCall();
	}

	@Test
	public void getBalanceSuccessfullyWithAccountCode() throws Exception {
		givenAValidAccountCode();
		whenCallGetBalance();
		thenExpectOkStatus();
		thenExpectAccountServiceGetBalanceCall();
	}

	@Test
	public void getAllAccountsSuccessfully() throws Exception {
		whenCallGetAllAccounts();
		thenExpectOkStatus();
		thenExpectAccountServiceGetAllCall();
	}

	@Test
	public void getAllAccountsSuccessfullyWithUserId() throws Exception {
		givenAValidUserId();
		whenCallGetAllAccountsByUserId();
		thenExpectOkStatus();
		thenExpectAccountServiceGetAllAccountsByUserIdCall();
	}

	@Test
	public void getAccountSuccessfullyWithAccountCode() throws Exception {
		givenAValidAccountCode();
		whenCallGetAccountByCode();
		thenExpectOkStatus();
		thenExpectAccountServiceGetAccountByCodeCall();
	}

	@Test
	public void getAccountSuccessfullyWithAccountId() throws Exception {
		givenAValidAccountId();
		whenCallGetAccountById();
		thenExpectOkStatus();
		thenExpectAccountServiceGetAccountByIdCall();
	}

	// Givens

	private void givenAValidAccountCode() {
		accountCode = UUID.randomUUID().toString();
	}

	private void givenAValidAccountId() {
		accountId = UUID.randomUUID().getMostSignificantBits();
	}

	private void givenAValidUserId() {
		userId = UUID.randomUUID().getMostSignificantBits();
	}

	private void givenValidAccountRequestTO() {
		accountRequestTO = new AccountRequestTO();
		accountRequestTO.setCode(UUID.randomUUID().toString());
		accountRequestTO.setUserId(UUID.randomUUID().getLeastSignificantBits());
	}

	private void givenInvalidAccountRequestTO() {
		accountRequestTO = null;
	}

	// Whens

	private void whenCallGetBalance() throws Exception {
		response = mockMvc.perform(get(ACCOUNTS_ENDPOINT + "/" + accountCode + "/balance")).andReturn().getResponse();
	}

	private void whenCallGetAllAccounts() throws Exception {
		response = mockMvc.perform(get(ACCOUNTS_ENDPOINT)).andReturn().getResponse();
	}

	private void whenCallPostCreateAccount() throws Exception {

		response = mockMvc.perform(post(ACCOUNTS_ENDPOINT).header("Content-Type", MediaType.APPLICATION_JSON)
				.content(objectToJson(accountRequestTO))).andReturn().getResponse();

	}

	private void whenCallGetAccountByCode() throws Exception {
		response = mockMvc.perform(get(ACCOUNTS_ENDPOINT + "/code/" + accountCode)).andReturn().getResponse();
	}

	private void whenCallGetAllAccountsByUserId() throws Exception {
		response = mockMvc.perform(get(ACCOUNTS_ENDPOINT + "/user/" + userId)).andReturn().getResponse();
	}

	private void whenCallGetAccountById() throws Exception {
		response = mockMvc.perform(get(ACCOUNTS_ENDPOINT + "/id/" + accountId)).andReturn().getResponse();
	}

	// Thens

	private void thenExpectOkStatus() {
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}

	private void thenExpectCreatedStatus() {
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
	}

	private void thenExpectBadRequestStatus() {
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private void thenExpectAccountServiceGetBalanceCall() throws AccountDoesNotExistsException {
		verify(accountService, times(1)).getBalance(accountCode);
	}

	private void thenExpectAccountServiceCreateAccountCall()
			throws AccountAlreadyExistsException, UserDoesNotExistException {
		verify(accountService, times(1)).createAccount(accountRequestTO);
	}

	private void thenExpectAccountServiceGetAllCall() {
		verify(accountService, times(1)).getAll();
	}

	private void thenExpectAccountServiceCreateAccountNoCall()
			throws AccountAlreadyExistsException, UserDoesNotExistException {
		verify(accountService, times(0)).createAccount(accountRequestTO);
	}

	private void thenExpectAccountServiceGetAccountByCodeCall() throws AccountDoesNotExistsException {
		verify(accountService, times(1)).getAccountByCode(accountCode);
	}

	private void thenExpectAccountServiceGetAllAccountsByUserIdCall() throws UserDoesNotExistException {
		verify(accountService, times(1)).getAllAccountsByUserId(userId);
	}

	private void thenExpectAccountServiceGetAccountByIdCall() throws AccountDoesNotExistsException {
		verify(accountService, times(1)).getAccountById(accountId);
	}

}
