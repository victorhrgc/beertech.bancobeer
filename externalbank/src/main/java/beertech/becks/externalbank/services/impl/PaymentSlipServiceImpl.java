package beertech.becks.externalbank.services.impl;

import beertech.becks.externalbank.services.PaymentSlipService;
import beertech.becks.externalbank.tos.request.PaymentSlipRequestTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PaymentSlipServiceImpl implements PaymentSlipService {

	@Value("${api.endpoint.create-payment-slip}")
	private String createPaymentSlipEndpoint;

	@Override
	public void callExternalEndpoint(PaymentSlipRequestTO to) {
		String encodedPaymentSlipCode = encode(to);

		WebClient client = WebClient.create(createPaymentSlipEndpoint);

		String requestJson = null;
		try {
			requestJson = new ObjectMapper().writeValueAsString(to);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		String ret = client.method(HttpMethod.POST).body(BodyInserters.fromValue(requestJson))
				.header("Content-Type", "application/json").exchange().block().bodyToMono(String.class).block();

	}

	private String encode(PaymentSlipRequestTO to) {
		return "";
	}

}
