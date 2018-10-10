package com.personal.rabbitmqcf.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personal.rabbitmqcf.vo.Message;

@Service
public class MessageSender {

	public static final String QUEUE_MESSAGE = "message-queue";
    public static final String EXCHANGE_MESSAGE = "message-exchange";
    
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
    public MessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
	
	public void sendMessage(Message message) {
		System.out.println("Message sending.");
		this.rabbitTemplate.convertAndSend(EXCHANGE_MESSAGE, QUEUE_MESSAGE, message);
	}
}
