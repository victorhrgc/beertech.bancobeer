package beertech.becks.api.controllers;

import static beertech.becks.api.constants.Constants.*;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import beertech.becks.api.entities.Account;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.transaction.InvalidTransactionOperationException;
import beertech.becks.api.service.TransactionService;
import beertech.becks.api.tos.request.TransactionRequestTO;
import beertech.becks.api.tos.request.TransferRequestTO;
import beertech.becks.api.tos.response.StatementResponseTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/transactions")
@Api(value = "Bank Becks Service")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@ApiIgnore
	@ApiResponses(value = { 
			@ApiResponse(code = 201, message = STATUS_201_CREATED),
			@ApiResponse(code = 400, message = STATUS_400_BAD_REQUEST),
			@ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR) })
	@PostMapping
	public ResponseEntity<Object> createTransaction(@Valid @RequestBody TransactionRequestTO transactionTO)
			throws AccountDoesNotExistsException, InvalidTransactionOperationException {
		transactionService.createTransaction(transactionTO);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiResponses(
			value = {
					@ApiResponse(code = 200, message = STATUS_200_GET_OK),
					@ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
					@ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
			})
	@GetMapping("/{accountCode}/statements")
	public ResponseEntity<StatementResponseTO> getAccountStatements(@PathVariable String accountCode)
			throws AccountDoesNotExistsException, InvalidTransactionOperationException {
		return new ResponseEntity<>(transactionService.getStatements(accountCode), HttpStatus.OK);
	}

	@ApiResponses(
			value = {
					@ApiResponse(code = 201, message = STATUS_201_CREATED),
					@ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
					@ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
			})
	@PostMapping("/{accountCode}/deposit")
	public ResponseEntity<Account> createDeposit(@PathVariable String accountCode, @RequestParam() BigDecimal value)
			throws AccountDoesNotExistsException {
		return new ResponseEntity<>(transactionService.createDeposit(accountCode, value), HttpStatus.CREATED);
	}

	@ApiResponses(
			value = {
					@ApiResponse(code = 201, message = STATUS_201_CREATED),
					@ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
					@ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
			})
	@PostMapping("/{accountCode}/withdrawal")
	public ResponseEntity<Account> createWithdrawal(@PathVariable String accountCode, @RequestParam BigDecimal value)
			throws AccountDoesNotExistsException {
		return new ResponseEntity<>(transactionService.createWithdrawal(accountCode, value), HttpStatus.CREATED);
	}

	@ApiResponses(
			value = {
					@ApiResponse(code = 201, message = STATUS_201_CREATED),
					@ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
					@ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
			})
	@PostMapping("/{accountCode}/transfer")
	public ResponseEntity<Account> createTransfer(@PathVariable String accountCode,
			@Valid @RequestBody TransferRequestTO transferRequestTO) throws AccountDoesNotExistsException {
		return new ResponseEntity<>(transactionService.createTransfer(accountCode, transferRequestTO),
				HttpStatus.CREATED);
	}

}
