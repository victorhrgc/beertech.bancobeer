package beertech.becks.api.controllers;

import static beertech.becks.api.constants.Constants.*;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import beertech.becks.api.exception.payment.PaymentSlipExecutionException;
import beertech.becks.api.service.PaymentSlipService;
import beertech.becks.api.tos.request.PaymentRequestTO;
import io.swagger.annotations.*;

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
    @GetMapping("/{userId}/paymentSlipsByUser")
    @ApiOperation(value = "Get payment slips by user document number", authorizations = @Authorization(value = "JWT"))
    public ResponseEntity<Object> getPaymentSlipsByUserDocumentNumber(@PathVariable Long userId) {
        return new ResponseEntity<>(paymentSlipService.findByUserId(userId), HttpStatus.OK);
    }

    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = STATUS_200_GET_OK),
                    @ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
                    @ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
            })
    @PostMapping("/{userId}/paymentSlips")
    @ApiOperation(value = "Executing payment", authorizations = @Authorization(value = "JWT"))
    public ResponseEntity<Object> executePayment(@Valid @RequestBody PaymentRequestTO paymentRequestTO) throws PaymentSlipExecutionException {
        return new ResponseEntity<>(paymentSlipService.executePayment(paymentRequestTO), HttpStatus.OK);
    }
}
