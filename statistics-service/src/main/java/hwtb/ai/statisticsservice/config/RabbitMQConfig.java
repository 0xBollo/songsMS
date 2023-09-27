package hwtb.ai.statisticsservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.queue.song-retrievals.name}")
    private String songRetrievalsQueueName;
    @Value("${rabbitmq.queue.song-updates.name}")
    private String songUpdatesQueueName;
    @Value("${rabbitmq.queue.song-deletions.name}")
    private String songDeletionsQueueName;
    @Value("${rabbitmq.routing-key.song-retrievals}")
    private String songRetrievalsRoutingKey;
    @Value("${rabbitmq.routing-key.song-updates}")
    private String songUpdatesRoutingKey;
    @Value("${rabbitmq.routing-key.song-deletions}")
    private String songDeletionsRoutingKey;

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue songRetrievalsQueue() {
        return new Queue(songRetrievalsQueueName);
    }

    @Bean
    public Queue songUpdatesQueue() {
        return new Queue(songUpdatesQueueName);
    }

    @Bean
    public Queue songDeletionsQueue() {
        return new Queue(songDeletionsQueueName);
    }

    @Bean
    public Binding songRetrievalsBinding() {
        return BindingBuilder.bind(songRetrievalsQueue())
                .to(exchange())
                .with(songRetrievalsRoutingKey);
    }

    @Bean
    public Binding songUpdatesBinding() {
        return BindingBuilder.bind(songUpdatesQueue())
                .to(exchange())
                .with(songUpdatesRoutingKey);
    }

    @Bean
    public Binding songDeletionsBinding() {
        return BindingBuilder.bind(songDeletionsQueue())
                .to(exchange())
                .with(songDeletionsRoutingKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
