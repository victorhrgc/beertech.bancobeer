package beertech.becks.api.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import beertech.becks.api.entities.PaymentSlip;
import beertech.becks.api.exception.payment.PaymentSlipExecutionException;
import beertech.becks.api.exception.user.UserDoesNotExistException;

public interface PaymentSlipService {

	List<PaymentSlip> findAll();

	List<PaymentSlip> findByUserId(Long userId) throws UserDoesNotExistException;

	PaymentSlip executePayment(String paymentCode) throws PaymentSlipExecutionException;

	void decodeAndSave(String paymentSlipCode) throws UnsupportedEncodingException, Exception;
}
