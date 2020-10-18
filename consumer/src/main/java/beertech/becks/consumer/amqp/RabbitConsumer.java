package beertech.becks.consumer.amqp;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import beertech.becks.consumer.services.ConsumerService;
import beertech.becks.consumer.tos.messages.DepositWithdrawalMessage;
import beertech.becks.consumer.tos.messages.StatementsMessage;
import beertech.becks.consumer.tos.messages.TransferMessage;

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
	private ConsumerService consumerService;

	/**
	 * Consumes the deposit message received from the queue
	 * 
	 * @param message the deposit message received from the queue
	 */
	@RabbitListener(queues = "${spring.rabbitmq.queue.deposit}")
	public void consumeDepositMessage(DepositWithdrawalMessage message) {
		consumerService.treatDepositMessage(message);
	}

	/**
	 * Consumes the withdrawal message received from the queue
	 *
	 * @param message the withdrawal message received from the queue
	 */
	@RabbitListener(queues = "${spring.rabbitmq.queue.withdrawal}")
	public void consumeWithdrawalMessage(DepositWithdrawalMessage message) {
		consumerService.treatWithdrawalMessage(message);
	}

	/**
	 * Consumes the transfer message received from the queue
	 *
	 * @param message the transfer message received from the queue
	 */
	@RabbitListener(queues = "${spring.rabbitmq.queue.transfer}")
	public void consumeTransferMessage(TransferMessage message) {
		consumerService.treatTransferMessage(message);
	}

	/**
	 * Consumes the statements message received from the queue
	 *
	 * @param message the statements message received from the queue
	 */
	@RabbitListener(queues = "${spring.rabbitmq.queue.statements}")
	public void consumeStatementsMessage(StatementsMessage message) {
		consumerService.treatStatementsMessage(message);
	}
}
