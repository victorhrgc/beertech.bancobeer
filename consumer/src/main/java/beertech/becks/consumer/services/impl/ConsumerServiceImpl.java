package beertech.becks.consumer.services.impl;

import beertech.becks.consumer.services.ConsumerService;
import beertech.becks.consumer.tos.Message;
import beertech.becks.consumer.tos.request.TransactionRequestTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
	 * The API endpoint
	 */
	@Value("${api.transactions.endpoint}")
	private String endpoint;

	/**
	 * Posts the received rabbit message to the API
	 * 
	 * @param message the rabbit message received
	 */
	@Override
	public void treatMessage(Message message) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		TransactionRequestTO requestTO = new TransactionRequestTO();
		requestTO.setDestinationAccountCode(message.getDestinationAccountCode());
		requestTO.setOperation(message.getOperation());
		requestTO.setOriginAccountCode(message.getOriginAccountCode());
		requestTO.setValue(message.getValue());

		// Receives a message and adds the time to it before sending to the API
		requestTO.setTransactionTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

		String json = null;
		try {
			json = new ObjectMapper().writeValueAsString(requestTO);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error converting message to json: " + e.getMessage());
		}

		new RestTemplate().postForLocation(endpoint, new HttpEntity<>(json, headers));
	}
}
