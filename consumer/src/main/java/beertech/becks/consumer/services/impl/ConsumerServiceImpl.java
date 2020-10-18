package beertech.becks.consumer.services.impl;

import beertech.becks.consumer.services.ConsumerService;

import beertech.becks.consumer.tos.messages.DepositWithdrawalMessage;
import beertech.becks.consumer.tos.messages.StatementsMessage;
import beertech.becks.consumer.tos.messages.TransferMessage;
import beertech.becks.consumer.tos.request.TransactionRequestTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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

		System.out.println(callApi(completeEndpoint, HttpMethod.POST, message.getJwtToken()));
	}

	@Override
	public void treatWithdrawalMessage(DepositWithdrawalMessage message) {
		String completeEndpoint = withdrawalEndpoint.replaceAll("accountCode", message.getAccountCode()) + "?value="
				+ message.getValue();

		System.out.println(callApi(completeEndpoint, HttpMethod.POST, message.getJwtToken()));
	}

	@Override
	public void treatTransferMessage(TransferMessage message) {
		//TODO make transfer requests to the API
		String completeEndpoint = transferEndpoint.replaceAll("accountCode", message.getOriginAccountCode());
		System.out.println("Endpoint: " + completeEndpoint);
		System.out.println("Message: " + message.toString());
	}

	@Override
	public void treatStatementsMessage(StatementsMessage message) {
		String completeEndpoint = statementsEndpoint.replaceAll("accountCode", message.getAccountCode());

		System.out.println(callApi(completeEndpoint, HttpMethod.GET, message.getJwtToken()));
	}

	private String callApi(String completeEndpoint, HttpMethod method, String jwtToken) {
		WebClient client = WebClient.create(completeEndpoint);

		return client.method(method).header("Authorization", "Bearer " + jwtToken).exchange().block()
				.bodyToMono(String.class).block();
	}

}
