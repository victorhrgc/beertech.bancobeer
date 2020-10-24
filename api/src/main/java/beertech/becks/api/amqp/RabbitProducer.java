package beertech.becks.api.amqp;

import java.util.concurrent.ExecutionException;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import beertech.becks.api.tos.request.PaymentRequestTO;
import beertech.becks.api.tos.response.PaymentResponseTO;

@Component
public class RabbitProducer {

	@Value("${spring.rabbitmq.exchange}")
	private String exchange;

	@Value("${spring.rabbitmq.routingkey.payment}")
	private String routingKey;

	@Autowired
	private AsyncRabbitTemplate rabbitTemplate;

	public void produceBlocking(PaymentRequestTO to) {
		try {
			ListenableFuture<PaymentResponseTO> listenableFuture = rabbitTemplate.convertSendAndReceiveAsType(exchange,
					routingKey, to, new ParameterizedTypeReference<PaymentResponseTO>() {
					});

			try {
				PaymentResponseTO resp = listenableFuture.get();
				// TODO
				System.out.println("Esperou a chamada bloqueante com sucesso!");
			} catch (InterruptedException | ExecutionException e) {
				// TODO
				System.out.println("Esperou a chamada bloqueante com erro!");
			}
		} catch (Exception e) {
			throw new AmqpRejectAndDontRequeueException(e);
		}
	}

	public void produceNonBlockingWithCallback(PaymentRequestTO to) {
		AsyncRabbitTemplate.RabbitConverterFuture<PaymentResponseTO> rabbitConverterFuture = rabbitTemplate
				.convertSendAndReceiveAsType(exchange, routingKey, to,
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
