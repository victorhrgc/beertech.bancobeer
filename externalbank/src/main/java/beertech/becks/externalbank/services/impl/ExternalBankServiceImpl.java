package beertech.becks.externalbank.services.impl;

import beertech.becks.externalbank.services.ExternalBankService;
import beertech.becks.externalbank.tos.messages.PaymentMessage;
import beertech.becks.externalbank.tos.response.PaymentResponseTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * The class implementing the description of the external bank services
 */
@Service
public class ExternalBankServiceImpl implements ExternalBankService {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = Logger.getLogger(ExternalBankServiceImpl.class.getName());

	@Override
	public PaymentResponseTO treatPaymentSlipMessage(PaymentMessage message) {
		PaymentResponseTO resp = new PaymentResponseTO();
		if (LocalDateTime.now().getMinute() % 2 == 0) {
			resp.setStatus("Erro");
		} else {
			resp.setStatus("Sucesso");
		}

		return resp;
	}

}
