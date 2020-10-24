package beertech.becks.externalbank.ampq;

import beertech.becks.externalbank.services.ExternalBankService;
import beertech.becks.externalbank.tos.messages.PaymentMessage;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The class that listens to the rabbit queue and treats the delivered message
 */
@Component
public class RabbitConsumer {
    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(RabbitConsumer.class.getName());

    /**
     * The consumer services
     */
    @Autowired
    private ExternalBankService externalBankService;

    /**
     * Consumes the payment slip message received from the queue
     *
     * @param message the payment slip message received from the queue
     */
    @RabbitListener(queues = "${spring.rabbitmq.queue.payment}")
    public void consumePaymentSlipMessage(PaymentMessage message) {
        externalBankService.treatPaymentSlipMessage(message);
    }

}
