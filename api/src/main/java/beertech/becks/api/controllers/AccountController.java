package beertech.becks.api.controllers;

import static beertech.becks.api.constants.Constants.*;

import javax.validation.Valid;

import beertech.becks.api.exception.user.UserDoesNotExistException;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import beertech.becks.api.entities.Account;
import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.service.AccountService;
import beertech.becks.api.tos.request.AccountRequestTO;
import beertech.becks.api.tos.response.BalanceResponseTO;

@RestController
@RequestMapping("/accounts")
@Api(value = "Bank Becks Service")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@ApiResponses(
			value = {
				@ApiResponse(code = 200, message = STATUS_200_POST_OK),
				@ApiResponse(code = 201, message = STATUS_201_CREATED),
				@ApiResponse(code = 400, message = STATUS_400_BAD_REQUEST),
				@ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
			})
	@PostMapping
	@ApiOperation(value = "Create account" , authorizations = @Authorization(value = "JWT"))
	public ResponseEntity<Object> createAccount(@Valid @RequestBody AccountRequestTO accountTO)
			throws AccountAlreadyExistsException, UserDoesNotExistException {
		Account createdAccount = accountService.createAccount(accountTO);
		return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
	}

	@ApiResponses(
			value = {
				@ApiResponse(code = 200, message = STATUS_200_GET_OK),
				@ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
				@ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
			})
	@GetMapping("/{accountCode}/balance")
	@ApiOperation(value = "Get account balance" , authorizations = @Authorization(value = "JWT"))
	public ResponseEntity<Object> getAccountBalance(@PathVariable String accountCode)
			throws AccountDoesNotExistsException {
		BalanceResponseTO balance = accountService.getBalance(accountCode);
		return new ResponseEntity<>(balance, HttpStatus.OK);
	}

	@ApiResponses(
			value = {
				@ApiResponse(code = 200, message = STATUS_200_GET_OK),
				@ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
				@ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
			})
	@GetMapping
	@ApiOperation(value = "Get all accounts" , authorizations = @Authorization(value = "JWT"))
	public ResponseEntity<Object> getAllAccounts() {
		return new ResponseEntity<>(accountService.getAll(), HttpStatus.OK);
	}

	@ApiResponses(
			value = {
					@ApiResponse(code = 200, message = STATUS_200_GET_OK),
					@ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
					@ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
			})
	@GetMapping("/user/{userId}")
	@ApiOperation(value = "Get all accounts by user id" , authorizations = @Authorization(value = "JWT"))
	public ResponseEntity<Object> getAllAccountsByUserId(@PathVariable Long userId) throws UserDoesNotExistException {
		return new ResponseEntity<>(accountService.getAllAccountsByUserId(userId), HttpStatus.OK);
	}

	@ApiResponses(
			value = {
				@ApiResponse(code = 200, message = STATUS_200_GET_OK),
				@ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
				@ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
			})
	@GetMapping("/id/{accountId}")
	@ApiOperation(value = "Get accounts by id" , authorizations = @Authorization(value = "JWT"))
	public ResponseEntity<Account> getAccountById(@PathVariable Long accountId) throws AccountDoesNotExistsException {
		return new ResponseEntity<>(accountService.getAccountById(accountId), HttpStatus.OK);
	}

	@ApiResponses(
			value = {
				@ApiResponse(code = 200, message = STATUS_200_GET_OK),
				@ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
				@ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
			})
	@GetMapping("/code/{accountCode}")
	@ApiOperation(value = "Get account by code" , authorizations = @Authorization(value = "JWT"))
	public ResponseEntity<Account> getAccountByCode(@PathVariable String accountCode) throws AccountDoesNotExistsException {
		return new ResponseEntity<>(accountService.getAccountByCode(accountCode), HttpStatus.OK);
	}
}