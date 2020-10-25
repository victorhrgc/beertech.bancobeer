package beertech.becks.externalbank.services.impl;

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

	private static final String pk = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCHHsiNllMhd29/6dFmkIuP2RpmUjlBsd/D438AEs8NCkSOoNmAm/2V7i3MfH6seQ/7VldtQyQXrvV7NkRvRfOKIsfLIKMzJyL3WAtWMvEB5WppJCEZICjPDUqsMdWNnVR4iEY6regVDQh9/DESmzZUeVsp+Gh5pFG8WAS8DhCphEskAVdJupMcQZQeK7u42hnwXS6TVmbux1Fvvaw4eP6wHqXbBqfD0KOXGUMmJlQkYq5G+xdfPHEvGAzWnXIeHkLE5uKOwTYLcJ2p2skR//SMiKXMnkkuAbUEI/Qz4e8ZNsz/px3UOiQaqDNkaCuOSWzKPsgVxIPJxZdf7KC3gndpAgMBAAECggEAYmLf1Xjn/y5gWdt/RhdqB8wllOu6FUPXk2NLTlpjuTMIyHIG7can10wevQ7Jjfx4zqIH9t80oKocxSK2oe9lbef1YsrZCp4sr73EQ3fEVYUe/n/4ObtY+6rJ/Kgsv3kX0MwplwCaRPnAfTvs34d0//19jAn8yNYFTgcpK2VybHt9RliQrM90a08Qav5NOjBz9tBke2r1VOAHenl6DJGFKHNJzUbCZWOrHSKNsPQa4n0G+cywAmXcEnPSe6k1jrCLRkVCEwIe07pPri65BM3J54PkFKjtLBBaNGfp8x5Drvt6mhn15Ar9xBcMFH0pdshhgahsHkHJYCp5yi/PcBocAQKBgQDbhWJFrS5vunG1OqKfb04jOXcJrlhOJOQVa+usQrm0h4w3aBsAMUg1PTfvaEV3hpxqqec5QCSt0GrhZgTOJMT5oVacGNl1A9TzoreNC2tEZoYDiqd3ESHNypTYX8xlbGZ9CxA5Evazy9KS1CCEvJR1Q0llGgvo1KeG+2wC0yHWkQKBgQCdkuqmois5mSgAkVA+iE3JcODDBfSxqPEHxX/rEeGzKnU8XJnFSqhF1Ud0Vv8j/q568J4M/eLwxjxbd3xwAIaube+XkoIo24azz3CePmwYc4e+PJKZhXoHCzKAqsv8BVAnU3RYbVAh50r2HwPliu1FwQPRlmfAad+879hqkFZvWQKBgFIciG/Loa0FLnc9oYrFlZKzcgVbA4jnX7FrltHue4w9j4NCG6dsx7eyWHPiqulUnrl/KBBQH+gsJ258PJvnQQ6m/MCSwvR0JHmlicbmWI9TUhmNCasN63kQp7Bv1QKzE3r+YmkW4NTMDs4BLtC3wUhDjPaWUZIbyTZCYVqFIwkhAoGBAJqtOQHRdrTXjtjfy/FWQ2VWFIvcMNKkj9WdHXBW4ULKjsJt89Qi8QN71ORy8YkX25Z60+rU3gyTy3PN2qznF03qRKJ5AqOwuo2PN3FevSVTYxBuzUk+KQb5Ct4aypndbUztMuwKgfqF3KO01lJhCYDLc0AtcDMEusfweHRCs1YhAoGAYs18vmi5nnhFU/0Mfegl8DZgELsE/eU6oVVQngnBtd5ooy53Jjw2enhBq+gOyDftqDXXonpg8d1bO3d2UWdQ4kWsU7sfpMLL4V4WSmE8hXQFOtoznP2cwD2nQf4YsUtwwuFc0ccFlwKMfoTnrBXdvinB+Yr8Z8e9o7DWQ0JPoaA=";

	@Value("${api.endpoint.create-payment-slip}")
	private String createPaymentSlipEndpoint;

	@Override
	public void callExternalEndpoint(PaymentSlipRequestTO to) {
		String encodedPaymentSlipCode = encode(to);

		WebClient client = WebClient.create(createPaymentSlipEndpoint);

		String requestJson = "{\"code\":\"" + encodedPaymentSlipCode + "\"}";

		PrivateKey privateKey = readPrivateKey(pk);

		byte[] signature = sign(requestJson.getBytes(), privateKey);

		String ret = client.method(HttpMethod.POST).body(BodyInserters.fromValue(requestJson))
				.header("Content-Type", "application/json").header("signature",signature.toString()).exchange().block().bodyToMono(String.class).block();
		System.out.println(ret);
	}

	private String encode(PaymentSlipRequestTO to) {
		String fullString = to.getDate() + "-" + to.getValue() + "-" + to.getCategory() + "-" + to.getOrigin() + "-"
				+ to.getDestination();
		return DatatypeConverter.printHexBinary(fullString.getBytes());
	}

	private PrivateKey readPrivateKey(String key) {
		byte[] encoded = Base64.getDecoder().decode(key);

		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");

			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
			return (PrivateKey) keyFactory.generatePrivate(keySpec);

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] sign(byte[] document, PrivateKey privateKey){

		try {
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initSign(privateKey);
			signature.update(document);
			return  signature.sign();
		} catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

}
