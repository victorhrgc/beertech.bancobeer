package beertech.becks.api.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import beertech.becks.api.entities.PaymentSlip;
import beertech.becks.api.exception.payment.PaymentSlipExecutionException;

public interface PaymentSlipService {

	List<PaymentSlip> findAll();

	Optional<PaymentSlip> findByUserId(Long userId);

    PaymentSlip executePayment(String paymentCode) throws PaymentSlipExecutionException;

	void decodeAndSave(String paymentSlipCode) throws UnsupportedEncodingException, Exception;
}
