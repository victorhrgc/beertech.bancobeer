package beertech.becks.api.service;

import java.io.UnsupportedEncodingException;
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
import beertech.becks.api.tos.response.PaymentSlipResponseTO;

public interface PaymentSlipService {

	List<PaymentSlipResponseTO> findAll();

	List<PaymentSlipResponseTO> findByUserId(Long userId) throws UserDoesNotExistException;

	void executePayment(String paymentCode) throws PaymentSlipDoesNotExistsException,
			AccountDoesNotHaveEnoughBalanceException, AccountDoesNotExistsException, PaymentNotDoneException;

	void decodeAndSave(String paymentSlipCode) throws PaymentSlipAlreadyExistsException, BankDoesNotExistsException,
			PaymentSlipRegisterException, AccountDoesNotExistsException;
}
