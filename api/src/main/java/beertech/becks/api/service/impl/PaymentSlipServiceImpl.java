package beertech.becks.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import beertech.becks.api.entities.PaymentSlip;
import beertech.becks.api.repositories.PaymentSlipRepository;
import beertech.becks.api.service.PaymentSlipService;

public class PaymentSlipServiceImpl implements PaymentSlipService {
	@Autowired
	private PaymentSlipRepository paymentSlipRepository;

	@Override
	public List<PaymentSlip> findAll() {
		return paymentSlipRepository.findAll();
	}

	@Override
	public List<PaymentSlip> findByUserDocumentNumber() {
		return new ArrayList<>();
	}

	@Override
	public void executePayment() {

	}
}
