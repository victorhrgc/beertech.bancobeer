package beertech.becks.api.controllers;

import static beertech.becks.api.constants.Constants.*;

import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.tos.response.ErrorResponseTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import beertech.becks.api.entities.Account;
import beertech.becks.api.service.AccountService;
import beertech.becks.api.tos.request.AccountRequestTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/accounts")
@Api(value = "Bank Becks Service")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = STATUS_200_POST_OK),
			@ApiResponse(code = 201, message = STATUS_201_CREATED),
			@ApiResponse(code = 400, message = STATUS_400_BAD_REQUEST),
			@ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR) })
	@PostMapping
	public ResponseEntity<Object> createAccount(@RequestBody AccountRequestTO accountTO) {
		try {
			Account createdAccount = accountService.createAccount(accountTO);
			return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
		} catch (AccountAlreadyExistsException e) {
			return new ResponseEntity<>(new ErrorResponseTO(e.getMessage()), HttpStatus.OK);
		}
	}
}