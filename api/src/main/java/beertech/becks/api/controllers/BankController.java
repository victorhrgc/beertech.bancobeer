package beertech.becks.api.controllers;

import static beertech.becks.api.constants.Constants.*;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import beertech.becks.api.exception.bank.BankAlreadyExistsException;
import beertech.becks.api.service.BankService;
import beertech.becks.api.tos.request.BankRequestTO;
import io.swagger.annotations.*;

@RestController
@RequestMapping("/banks")
@Api(value = "Bank Becks Service")
@CrossOrigin(origins = "*")
public class BankController {

    @Autowired
    private BankService bankService;

    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = STATUS_200_GET_OK),
                    @ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
                    @ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
            })
    @GetMapping
    @ApiOperation(value = "Get all banks" , authorizations = @Authorization(value = "JWT"))
    public ResponseEntity<Object> getAllBanks() {
        return new ResponseEntity<>(bankService.findAll(), HttpStatus.OK);
    }

    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = STATUS_200_POST_OK),
                    @ApiResponse(code = 201, message = STATUS_201_CREATED),
                    @ApiResponse(code = 400, message = STATUS_400_BAD_REQUEST),
                    @ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
                    @ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
            })
	@PostMapping
	@ApiOperation(value = "Create bank", authorizations = @Authorization(value = "JWT"))
	public ResponseEntity<Object> createBank(@Valid @RequestBody BankRequestTO bankTO)
			throws BankAlreadyExistsException {
		return new ResponseEntity<>(bankService.createBank(bankTO), HttpStatus.CREATED);
	}
}
