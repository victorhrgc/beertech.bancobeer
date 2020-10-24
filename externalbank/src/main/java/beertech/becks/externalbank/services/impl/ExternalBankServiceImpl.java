package beertech.becks.externalbank.services.impl;

import beertech.becks.externalbank.services.ExternalBankService;
import beertech.becks.externalbank.tos.messages.PaymentMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

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

    /**
     * The API endpoints
     */

    @Value("${api.endpoint.payment-slips}")
    private String paymentSlipEndpoint;

    @Override
    public void treatPaymentSlipMessage(PaymentMessage message) {
        String completeEndpoint = paymentSlipEndpoint.replaceAll("code", message.getCode()) ;

        System.out.println(callApi(completeEndpoint, HttpMethod.POST, message.getJwtToken(), null));
    }

    private String callApi(String completeEndpoint, HttpMethod method, String jwtToken, String jsonBody) {
        WebClient client = WebClient.create(completeEndpoint);

        if (jsonBody != null) {
            return client.method(HttpMethod.POST).body(BodyInserters.fromValue(jsonBody))
                    .header("Authorization", "Bearer " + jwtToken).header("Content-Type", "application/json").exchange()
                    .block().bodyToMono(String.class).block();
        }

        return client.method(method).header("Authorization", "Bearer " + jwtToken).exchange().block()
                .bodyToMono(String.class).block();
    }

}
