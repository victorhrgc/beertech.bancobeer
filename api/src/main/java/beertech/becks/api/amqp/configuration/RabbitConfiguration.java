package beertech.becks.api.amqp.configuration;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RabbitConfiguration {
	@Autowired
	private ConnectionFactory connectionFactory;

	private RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jacksonConverter());
		return rabbitTemplate;
	}

	@Bean
	public AsyncRabbitTemplate asyncRabbitTemplate() {
		return new AsyncRabbitTemplate(rabbitTemplate());
	}

	@Bean
	MessageConverter jacksonConverter() {
		return new Jackson2JsonMessageConverter(Jackson2ObjectMapperBuilder.json().modules(new JavaTimeModule())
				.dateFormat(new StdDateFormat()).build());
	}

}
