package beertech.becks.externalbank.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import beertech.becks.externalbank.services.PaymentSlipService;
import beertech.becks.externalbank.tos.request.PaymentSlipRequestTO;

@RestController
@RequestMapping("/payment-slips")
@CrossOrigin(origins = "*")
public class PaymentSlipController {

	@Autowired
	private PaymentSlipService paymentSlipService;

	@PostMapping
	public void createPaymentSlip(@RequestBody PaymentSlipRequestTO requestTO) {
		paymentSlipService.callExternalEndpoint(requestTO);
	}
}
