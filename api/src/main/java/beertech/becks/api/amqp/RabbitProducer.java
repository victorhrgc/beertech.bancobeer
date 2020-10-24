package beertech.becks.api.amqp;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import beertech.becks.api.tos.response.PaymentResponseTO;

@Component
public class RabbitProducer {

	@Value("${spring.rabbitmq.exchange}")
	private String exchange;

	@Value("${spring.rabbitmq.routingkey.payment}")
	private String routingKey;

	@Autowired
	private AsyncRabbitTemplate rabbitTemplate;

	public boolean produceBlockingMessageSuccessfully(String paymentCode) {
		try {
			ListenableFuture<PaymentResponseTO> listenableFuture = rabbitTemplate.convertSendAndReceiveAsType(exchange,
					routingKey, paymentCode, new ParameterizedTypeReference<PaymentResponseTO>() {
					});

			PaymentResponseTO resp = listenableFuture.get();
			if (resp.getStatus().toUpperCase().equals("ERRO")) {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void produceNonBlockingWithCallback(String paymentCode) {
		AsyncRabbitTemplate.RabbitConverterFuture<PaymentResponseTO> rabbitConverterFuture = rabbitTemplate
				.convertSendAndReceiveAsType(exchange, routingKey, paymentCode,
						new ParameterizedTypeReference<PaymentResponseTO>() {
						});

		rabbitConverterFuture.addCallback(new ListenableFutureCallback<PaymentResponseTO>() {
			@Override
			public void onFailure(Throwable ex) {
				// TODO
				System.out.println("Voltou do callback falha!");
			}

			@Override
			public void onSuccess(PaymentResponseTO to) {
				// TODO
				System.out.println("Voltou do callback sucesso!");
			}
		});
	}
}
