package beertech.becks.consumer.configuration;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration class for our rabbit queue
 */
@Configuration
public class RabbitConfiguration {

	//TODO fazer criar a fila automaticamente

	/**
	 * The jackson converter to convert json messages into objects
	 * 
	 * @return a Jackson2JsonMessageConverter instance
	 */
	@Bean
	MessageConverter jacksonConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
