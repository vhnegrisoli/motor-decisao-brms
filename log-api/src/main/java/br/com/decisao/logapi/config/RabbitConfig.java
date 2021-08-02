package br.com.decisao.logapi.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${rabbit.exchange.log-data}")
    private String logDataTopicExchange;

    @Value("${rabbit.queue.log-data}")
    private String logDataMq;

    @Value("${rabbit.routingkey.log-data}")
    private String logDataKey;

    @Bean
    public TopicExchange topic() {
        return new TopicExchange(logDataTopicExchange);
    }

    @Bean
    Queue logDataMq() {
        return new Queue(logDataMq, true);
    }

    @Bean
    public Binding logDataMqBinding(TopicExchange exchange) {
        return BindingBuilder
            .bind(logDataMq())
            .to(exchange)
            .with(logDataKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
