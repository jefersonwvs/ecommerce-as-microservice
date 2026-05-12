package dev.jefersonwvs.msorder.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

  static final String EXCHANGE = "ecommerce-events";
  static final String ORDER_CREATED_QUEUE = "payment.order-created";
  static final String ORDER_CREATED_ROUTING_KEY = "order.created";

  @Bean
  public TopicExchange topicExchange() {
    return new TopicExchange(EXCHANGE);
  }

  @Bean
  public Queue orderCreatedQueue() {
    return new Queue(ORDER_CREATED_QUEUE);
  }

  @Bean
  public Binding orderCreatedBinding() {
    return BindingBuilder.bind(orderCreatedQueue()).to(topicExchange()).with(ORDER_CREATED_ROUTING_KEY);
  }

  @Bean
  public MessageConverter messageConverter() {
    return new JacksonJsonMessageConverter();
  }
}
