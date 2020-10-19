package beertech.becks.consumer.services.impl;

import beertech.becks.consumer.tos.request.TransferRequestTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import beertech.becks.consumer.services.ConsumerService;
import beertech.becks.consumer.tos.messages.DepositWithdrawalMessage;
import beertech.becks.consumer.tos.messages.StatementsMessage;
import beertech.becks.consumer.tos.messages.TransferMessage;

/**
 * The class implementing the description of the consumer services
 */
@Service
public class ConsumerServiceImpl implements ConsumerService {
	/**
	 * Logger
	 */
	private static final Logger LOGGER = Logger.getLogger(ConsumerServiceImpl.class.getName());

	/**
	 * The API endpoints
	 */

	@Value("${api.endpoint.deposit}")
	private String depositEndpoint;

	@Value("${api.endpoint.withdrawal}")
	private String withdrawalEndpoint;

	@Value("${api.endpoint.transfer}")
	private String transferEndpoint;

	@Value("${api.endpoint.statements}")
	private String statementsEndpoint;

	@Override
	public void treatDepositMessage(DepositWithdrawalMessage message) {
		String completeEndpoint = depositEndpoint.replaceAll("accountCode", message.getAccountCode()) + "?value="
				+ message.getValue();

		System.out.println(callApi(completeEndpoint, HttpMethod.POST, message.getJwtToken(), null));
	}

	@Override
	public void treatWithdrawalMessage(DepositWithdrawalMessage message) {
		String completeEndpoint = withdrawalEndpoint.replaceAll("accountCode", message.getAccountCode()) + "?value="
				+ message.getValue();

		System.out.println(callApi(completeEndpoint, HttpMethod.POST, message.getJwtToken(), null));
	}

	@Override
	public void treatTransferMessage(TransferMessage message) {
		String completeEndpoint = transferEndpoint.replaceAll("accountCode", message.getOriginAccountCode());

		String requestJson = null;
		try {
			requestJson = new ObjectMapper().writeValueAsString(TransferRequestTO.builder().value(message.getValue())
					.destinationAccountCode(message.getDestinationAccountCode()).build());
		} catch (JsonProcessingException e) {
			LOGGER.error("Error converting message to json: " + e.getMessage());
		}

		System.out.println(callApi(completeEndpoint, HttpMethod.POST, message.getJwtToken(), requestJson));
	}

	@Override
	public void treatStatementsMessage(StatementsMessage message) {
		String completeEndpoint = statementsEndpoint.replaceAll("accountCode", message.getAccountCode());

		System.out.println(callApi(completeEndpoint, HttpMethod.GET, message.getJwtToken(), null));
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
