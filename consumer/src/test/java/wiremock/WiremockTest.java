package wiremock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import beertech.becks.consumer.services.impl.ConsumerServiceImpl;
import beertech.becks.consumer.tos.messages.StatementsMessage;

@ExtendWith(MockitoExtension.class)
public class WiremockTest {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);

	@Test
	public void getStatementsSuccessfuly() {
		ConsumerServiceImpl service = new ConsumerServiceImpl();
		ReflectionTestUtils.setField(service, "statementsEndpoint", "http://localhost:8089/transactions/1/statements");

		stubFor(get(urlEqualTo("/transactions/1/statements")).withHeader("accept", equalTo("*/*"))
				.willReturn(aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody("{\n" + "  \"accountStatements\": [],\n" + "  \"balance\": 50\n" + "}")));

		StatementsMessage message = new StatementsMessage();
		message.setAccountCode("1");
		message.setJwtToken("asdsa");

		String ret = service.treatStatementsMessage(message);

		assertEquals("Sucesso", ret);
	}

	@Test
	public void getStatementsUnsuccessfuly() {
		ConsumerServiceImpl service = new ConsumerServiceImpl();
		ReflectionTestUtils.setField(service, "statementsEndpoint", "http://localhost:8089/transactions/1/statements");

		stubFor(get(urlEqualTo("/transactions/1/statements")).withHeader("accept", equalTo("*/*"))
				.willReturn(aResponse().withStatus(500)));

		StatementsMessage message = new StatementsMessage();
		message.setAccountCode("1");
		message.setJwtToken("asdsa");

		String ret = service.treatStatementsMessage(message);

		assertEquals("Erro", ret);
	}
}
