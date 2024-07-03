package com.test.test;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
	@Autowired
	private SimpMessagingTemplate template;
	private MessageLog logs = new MessageLog();
	@MessageMapping("/chat")
	//@SendTo("/chat")
	public Message hi(@Payload Message message) {
		logs.getLog().add(message);
		return message;
	}
	@MessageMapping("/join")
	public @ResponseBody void join(@Payload User user) {
		template.convertAndSend("/chat.join." + user.getUsername(), logs);
	}
}
