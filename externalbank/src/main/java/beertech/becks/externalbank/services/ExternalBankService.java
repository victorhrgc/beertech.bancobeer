package beertech.becks.externalbank.services;

import beertech.becks.externalbank.tos.messages.PaymentMessage;
import beertech.becks.externalbank.tos.response.PaymentResponseTO;

/**
 * The class implementing the description of the external bank services
 */
public interface ExternalBankService {
	PaymentResponseTO treatPaymentSlipMessage(PaymentMessage message);
}
