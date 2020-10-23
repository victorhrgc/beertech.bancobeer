package beertech.becks.api.controllers;

import beertech.becks.api.service.AccountService;
import beertech.becks.api.service.PaymentSlipService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static beertech.becks.api.constants.Constants.*;

@RestController
@RequestMapping("/payment-slips")
@Api(value = "Bank Becks Service")
@CrossOrigin(origins = "*")
public class PaymentSlipController {

    @Autowired
    private PaymentSlipService paymentSlipService;

    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = STATUS_200_GET_OK),
                    @ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
                    @ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
            })
	@GetMapping
	@ApiOperation(value = "Get all payment slips", authorizations = @Authorization(value = "JWT"))
	public ResponseEntity<Object> getAllPaymentSlips() {
		return new ResponseEntity<>(paymentSlipService.findAll(), HttpStatus.OK);
	}
}
