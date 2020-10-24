package beertech.becks.externalbank.services;

import beertech.becks.externalbank.tos.request.PaymentSlipRequestTO;

public interface PaymentSlipService {
	void callExternalEndpoint(PaymentSlipRequestTO to);
}
