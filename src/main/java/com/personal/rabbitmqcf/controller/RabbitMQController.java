package com.personal.rabbitmqcf.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personal.rabbitmqcf.service.MessageSender;
import com.personal.rabbitmqcf.vo.Message;

@RestController
public class RabbitMQController {
	
	@Autowired
	private MessageSender messageSender;
	
	@RequestMapping("/getString")
	public String getString() {
		return "Successfully returned ";
	}
	
	@RequestMapping("/sendMessage")
	public String sendMessage() {
		Message message = new Message();
		Random rand = new Random();
		int  n = rand.nextInt(50) + 1;
		message.setId(n);
		message.setName("River"+n);
		message.setSalary(43232F);
		messageSender.sendMessage(message);
		return "Successfull";
	}
}
