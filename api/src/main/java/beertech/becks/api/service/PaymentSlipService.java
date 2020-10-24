package beertech.becks.api.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import beertech.becks.api.entities.PaymentSlip;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.account.AccountDoesNotHaveEnoughBalanceException;
import beertech.becks.api.exception.payment.PaymentNotDoneException;
import beertech.becks.api.exception.paymentslip.PaymentSlipDoesNotExistsException;
import beertech.becks.api.exception.user.UserDoesNotExistException;

public interface PaymentSlipService {

	List<PaymentSlip> findAll();

	List<PaymentSlip> findByUserId(Long userId) throws UserDoesNotExistException;

	void executePayment(String paymentCode) throws PaymentSlipDoesNotExistsException,
			AccountDoesNotHaveEnoughBalanceException, AccountDoesNotExistsException, PaymentNotDoneException;

	void decodeAndSave(String paymentSlipCode) throws Exception;
}
