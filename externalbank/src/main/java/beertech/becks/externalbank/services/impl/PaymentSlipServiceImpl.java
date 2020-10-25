package beertech.becks.externalbank.services.impl;

import beertech.becks.externalbank.security.DigitalSign;
import io.netty.handler.codec.base64.Base64Encoder;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import beertech.becks.externalbank.services.PaymentSlipService;
import beertech.becks.externalbank.tos.request.PaymentSlipRequestTO;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Service
public class PaymentSlipServiceImpl implements PaymentSlipService {


	@Value("${api.endpoint.create-payment-slip}")
	private String createPaymentSlipEndpoint;

	@Override
	public void callExternalEndpoint(PaymentSlipRequestTO to) {
		String encodedPaymentSlipCode = encode(to);

		WebClient client = WebClient.create(createPaymentSlipEndpoint);

		String requestJson = "{\"code\":\"" + encodedPaymentSlipCode + "\"}";

		PrivateKey privateKey = DigitalSign.getPrivateKey();

		byte[] signature = DigitalSign.sign(requestJson.getBytes(), privateKey);
		String base64signature = Base64.getEncoder().encodeToString(signature);

		String ret = client.method(HttpMethod.POST).body(BodyInserters.fromValue(requestJson))
				.header("Content-Type", "application/json")
				.header("signature",base64signature)
				.exchange().block().bodyToMono(String.class).block();
		System.out.println(ret);
	}

	private String encode(PaymentSlipRequestTO to) {
		String fullString = to.getDate() + "-" + to.getValue() + "-" + to.getCategory() + "-" + to.getOrigin() + "-"
				+ to.getDestination();
		return DatatypeConverter.printHexBinary(fullString.getBytes());
	}
}
