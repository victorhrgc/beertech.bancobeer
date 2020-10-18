package beertech.becks.consumer.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * The configuration class for our rabbit queue
 */
@Configuration
public class RabbitConfiguration {

	/**
	 * The name of the exchange
	 */
	@Value("${spring.rabbitmq.exchange}")
	private String exchange;

	/**
	 * The transaction queues
	 */
	@Value("${spring.rabbitmq.queue.deposit}")
	private String depositQueue;

	@Value("${spring.rabbitmq.queue.withdrawal}")
	private String withdrawalQueue;

	@Value("${spring.rabbitmq.queue.transfer}")
	private String transferQueue;

	@Value("${spring.rabbitmq.queue.statements}")
	private String statementsQueue;

	/**
	 * The transaction routing keys
	 */
	@Value("${spring.rabbitmq.routingkey.deposit}")
	private String depositRk;

	@Value("${spring.rabbitmq.routingkey.withdrawal}")
	private String withdrawalRk;

	@Value("${spring.rabbitmq.routingkey.transfer}")
	private String transferRk;

	@Value("${spring.rabbitmq.routingkey.statements}")
	private String statementsRk;

	/**
	 * The jackson converter to convert json messages into objects
	 * 
	 * @return a Jackson2JsonMessageConverter instance
	 */
	@Bean
	MessageConverter jacksonConverter() {
		return new Jackson2JsonMessageConverter();
	}

	/**
	 * Creates the exchange
	 */

	@Bean
	public DirectExchange exchange() {
		return new DirectExchange(exchange);
	}

	/**
	 * Creates our queues
	 */

	@Bean
	public Queue depositQueue() {
		Map<String, Object> args = new HashMap<>();
		return new Queue(depositQueue, true, false, false, args);
	}

	@Bean
	public Queue withdrawalQueue() {
		Map<String, Object> args = new HashMap<>();
		return new Queue(withdrawalQueue, true, false, false, args);
	}

	@Bean
	public Queue transferQueue() {
		Map<String, Object> args = new HashMap<>();
		return new Queue(transferQueue, true, false, false, args);
	}

	@Bean
	public Queue statementsQueue() {
		Map<String, Object> args = new HashMap<>();
		return new Queue(statementsQueue, true, false, false, args);
	}

	/**
	 * Does all the queue bindings
	 */

	@Bean
	public Binding bindDepositQueue() {
		return BindingBuilder.bind(depositQueue()).to(exchange()).with(depositRk);
	}

	@Bean
	public Binding bindWithdrawalQueue() {
		return BindingBuilder.bind(withdrawalQueue()).to(exchange()).with(withdrawalRk);
	}

	@Bean
	public Binding bindTransferQueue() {
		return BindingBuilder.bind(transferQueue()).to(exchange()).with(transferRk);
	}

	@Bean
	public Binding bindStatementsQueue() {
		return BindingBuilder.bind(statementsQueue()).to(exchange()).with(statementsRk);
	}

}
