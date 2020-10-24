package beertech.becks.api.service;

import java.util.List;
import java.util.Optional;

import beertech.becks.api.entities.PaymentSlip;
import beertech.becks.api.exception.payment.PaymentSlipExecutionException;
import beertech.becks.api.tos.request.PaymentSlipRequestTO;

public interface PaymentSlipService {

	List<PaymentSlip> findAll();

	Optional<PaymentSlip> findByUserDocumentNumber(String documentNumber);

    PaymentSlip executePayment(PaymentSlipRequestTO paymentSlipRequestTO) throws PaymentSlipExecutionException;

}