package beertech.becks.externalbank.ampq;

import java.time.LocalDateTime;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import beertech.becks.externalbank.tos.request.PaymentRequestTO;
import beertech.becks.externalbank.tos.response.PaymentResponseTO;

/**
 * The class that listens to the rabbit queue and treats the delivered message
 */
@Component
public class RabbitConsumer {
	/**
	 * treatPaymentSlipMessage Logger
	 */
	private static final Logger LOGGER = Logger.getLogger(RabbitConsumer.class.getName());

	/**
	 * Consumes the payment slip message received from the queue
	 *
	 * @param message the payment slip message received from the queue
	 */
	@RabbitListener(queues = "${spring.rabbitmq.queue.payment}")
	public PaymentResponseTO consumePaymentSlipMessage(PaymentRequestTO message) {
		PaymentResponseTO resp = new PaymentResponseTO();
		if (LocalDateTime.now().getMinute() % 2 == 0) {
			resp.setStatus("Erro");
		} else {
			resp.setStatus("Sucesso");
		}

		return resp;
	}

}
