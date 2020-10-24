package beertech.becks.externalbank.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
     * The payment slip queues
     */
    @Value("${spring.rabbitmq.queue.payment}")
    private String paymentQueue;

    /**
     * The payment slip routing keys
     */
    @Value("${spring.rabbitmq.routingkey.payment}")
    private String paymentRk;


    public RabbitConfiguration() {
    }

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
    public Queue paymentQueue() {
        Map<String, Object> args = new HashMap<>();
        return new Queue(paymentQueue, true, false, false, args);
    }

    /**
     * Does all the queue bindings
     */

    @Bean
    public Binding bindPaymentQueue() {
        return BindingBuilder.bind(paymentQueue()).to(exchange()).with(paymentRk);
    }

}
