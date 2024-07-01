package com.test.test;

import java.util.ArrayList;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {
	@MessageMapping("/chat")
	//@SendTo("/chat")
	public Message hi(@Payload Message message) {
		MessageLog.getLog().add(message);
		return message;
	}
	@MessageMapping("/join")
	@SendTo("/chat.join")
	public ArrayList<Message> join() {
		System.out.println("aaaaaa");
		return MessageLog.getLog();
	}
}
