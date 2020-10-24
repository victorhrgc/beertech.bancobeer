package beertech.becks.externalbank.services;

import beertech.becks.externalbank.tos.messages.PaymentMessage;

/**
 * The class implementing the description of the external bank services
 */
public interface ExternalBankService {
    void treatPaymentSlipMessage(PaymentMessage message);
}
