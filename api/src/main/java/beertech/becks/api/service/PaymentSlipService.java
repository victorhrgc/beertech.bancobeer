package beertech.becks.api.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import beertech.becks.api.entities.PaymentSlip;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.account.AccountDoesNotHaveEnoughBalanceException;
import beertech.becks.api.exception.bank.BankDoesNotExistsException;
import beertech.becks.api.exception.payment.PaymentNotDoneException;
import beertech.becks.api.exception.paymentslip.PaymentSlipAlreadyExistsException;
import beertech.becks.api.exception.paymentslip.PaymentSlipDoesNotExistsException;
import beertech.becks.api.exception.paymentslip.PaymentSlipRegisterException;
import beertech.becks.api.exception.user.UserDoesNotExistException;
import beertech.becks.api.tos.request.PaymentSlipTO;
import beertech.becks.api.tos.response.PaymentSlipResponseTO;

public interface PaymentSlipService {

	List<PaymentSlipResponseTO> findAll();

	List<PaymentSlipResponseTO> findByUserId(Long userId) throws UserDoesNotExistException;

	void executePayment(String paymentCode) throws PaymentSlipDoesNotExistsException,
			AccountDoesNotHaveEnoughBalanceException, AccountDoesNotExistsException, PaymentNotDoneException;

	void decodeAndSave(PaymentSlipTO paymentSlip, String signature) throws PaymentSlipAlreadyExistsException, BankDoesNotExistsException,
			PaymentSlipRegisterException, AccountDoesNotExistsException, InvalidKeySpecException, NoSuchAlgorithmException;
}
