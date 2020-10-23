package beertech.becks.api.service;

import java.util.List;

import beertech.becks.api.entities.PaymentSlip;

public interface PaymentSlipService {

	List<PaymentSlip> findAll();
}
