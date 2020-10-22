package beertech.becks.api.controllers;

import static beertech.becks.api.constants.Constants.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import beertech.becks.api.entities.Account;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.account.AccountDoesNotHaveEnoughBalanceException;
import beertech.becks.api.service.TransactionService;
import beertech.becks.api.tos.request.TransferRequestTO;
import beertech.becks.api.tos.response.StatementResponseTO;
import io.swagger.annotations.*;

@RestController
@RequestMapping("/transactions")
@Api(value = "Bank Becks Service")
@CrossOrigin(origins = "*")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@ApiResponses(value = { @ApiResponse(code = 200, message = STATUS_200_GET_OK),
			@ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
			@ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR) })
	@GetMapping("/{accountCode}/statements")
	@ApiOperation(value = "Get account statements", authorizations = @Authorization(value = "JWT"))
	public ResponseEntity<StatementResponseTO> getAccountStatements(@PathVariable String accountCode)
			throws AccountDoesNotExistsException {
		return ok(transactionService.getStatements(accountCode));
	}

	@ApiResponses(value = { @ApiResponse(code = 201, message = STATUS_201_CREATED),
			@ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
			@ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR) })
	@PostMapping("/{accountCode}/deposit")
	@ApiOperation(value = "Create deposit", authorizations = @Authorization(value = "JWT"))
	public ResponseEntity<Account> createDeposit(@PathVariable String accountCode, @RequestParam() BigDecimal value)
			throws AccountDoesNotExistsException {
		return new ResponseEntity<>(transactionService.createDeposit(accountCode, value), CREATED);
	}

	@ApiResponses(value = { @ApiResponse(code = 201, message = STATUS_201_CREATED),
			@ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
			@ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR) })
	@PostMapping("/{accountCode}/withdrawal")
	@ApiOperation(value = "Create withdrawal", authorizations = @Authorization(value = "JWT"))
	public ResponseEntity<Account> createWithdrawal(@PathVariable String accountCode, @RequestParam BigDecimal value)
			throws AccountDoesNotExistsException, AccountDoesNotHaveEnoughBalanceException {
		return new ResponseEntity<>(transactionService.createWithdrawal(accountCode, value), CREATED);
	}

	@ApiResponses(value = { @ApiResponse(code = 201, message = STATUS_201_CREATED),
			@ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
			@ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR) })
	@PostMapping("/{accountCode}/transfer")
	@ApiOperation(value = "Create transfer", authorizations = @Authorization(value = "JWT"))
	public ResponseEntity<Account> createTransfer(@PathVariable String accountCode,
			@Valid @RequestBody TransferRequestTO transferRequestTO)
			throws AccountDoesNotExistsException, AccountDoesNotHaveEnoughBalanceException {
		return new ResponseEntity<>(transactionService.createTransfer(accountCode, transferRequestTO), CREATED);
	}

}
