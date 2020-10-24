package beertech.becks.api.controllers;

import beertech.becks.api.exception.payment.PaymentSlipExecutionException;
import beertech.becks.api.service.PaymentSlipService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = STATUS_200_GET_OK),
                    @ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
                    @ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
            })
    @GetMapping("/user/{userId}")
    @ApiOperation(value = "Get payment slips by user document number", authorizations = @Authorization(value = "JWT"))
    public ResponseEntity<Object> getPaymentSlipsByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(paymentSlipService.findByUserId(userId), HttpStatus.OK);
    }

    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = STATUS_200_GET_OK),
                    @ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
                    @ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
            })
    @PostMapping("/{paymentCode}")
    @ApiOperation(value = "Executing payment", authorizations = @Authorization(value = "JWT"))
    public ResponseEntity<Object> executePayment(@PathVariable String paymentCode) throws PaymentSlipExecutionException {
        return new ResponseEntity<>(paymentSlipService.executePayment(paymentCode), HttpStatus.OK);
    }
}
