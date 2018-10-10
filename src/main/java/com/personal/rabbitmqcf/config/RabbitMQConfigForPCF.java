package com.personal.rabbitmqcf.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.DirectRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties.Listener;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@Configuration
public class RabbitMQConfigForPCF extends AbstractCloudConfig implements RabbitListenerConfigurer {

	public static final String QUEUE_MESSAGE = "message-queue";
	public static final String EXCHANGE_MESSAGE = "message-exchange";

	@Bean
	public ConnectionFactory rabbitFactory() {
		RabbitConnectionFactoryConfig rabbitConfig = new RabbitConnectionFactoryConfig(10);
		return connectionFactory().rabbitConnectionFactory("rabbitmq-test", rabbitConfig);
	}

	@Bean
	public Exchange exchange() {
		return ExchangeBuilder.directExchange(EXCHANGE_MESSAGE).build();
	}

	@Bean
	Queue queue() {
		return QueueBuilder.durable(QUEUE_MESSAGE).build();
	}

	@Bean
	Binding binding(Queue messageQueue, DirectExchange messageExchange) {
		return BindingBuilder.bind(messageQueue).to(messageExchange).with(QUEUE_MESSAGE);
	}

	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
		return rabbitTemplate;
	}

	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Override
	public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
		registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
	}

	@Bean
	public MessageHandlerMethodFactory messageHandlerMethodFactory() {
		DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
		messageHandlerMethodFactory.setMessageConverter(consumerJackson2MessageConverter());
		return messageHandlerMethodFactory;
	}

	@Bean
	public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
		return new MappingJackson2MessageConverter();
	}

	@Bean
	public DirectRabbitListenerContainerFactory rabbitListenerContainerFactory(
			DirectRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
		DirectRabbitListenerContainerFactory factory = new DirectRabbitListenerContainerFactory();
		factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		factory.setPrefetchCount(1);
		configurer.configure(factory, connectionFactory);
		return factory;
	}

	@Bean
	public Listener listener() {
		return new Listener();
	}

}
