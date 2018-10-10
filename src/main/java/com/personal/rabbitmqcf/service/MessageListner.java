package com.personal.rabbitmqcf.service;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.personal.rabbitmqcf.vo.Message;
import com.rabbitmq.client.Channel;

@Component
public class MessageListner {

	public static final String QUEUE_MESSAGE = "message-queue"; 
  
	@RabbitListener(queues = QUEUE_MESSAGE)
	public void receiveMessage(Message payload, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag)
			throws IOException {
		try {
			System.out.println("New Received payload" + payload);
			// Teradata
			// SQL 			
			channel.basicAck(deliveryTag, false);
 		} catch (Throwable ex) {
			System.out.println("Exception caught.."+ ex);
			channel.basicReject(deliveryTag, true);
		}
		// Email
	}
}
