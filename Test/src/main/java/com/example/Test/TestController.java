package com.example.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Controller
public class TestController {
	@Autowired
	private SimpMessagingTemplate template;
	private ChatRepository repos = new ChatRepository();
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TestController.class);

	
	@MessageMapping("/chat/join")
	@SendTo("/aa")
	public Message join(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
		message.setMessage(message.getSender() + " has joined.");
		
		for(int i = 0; i<repos.getMessagecount(); i++) {
			log.info(repos.getMessage()[i].getMessage());
		}
		log.info("");
		repos.addMessage(message);
		return message;
	}
	
	
	@MessageMapping("/chat/join/update")
	public void update(@Payload Message message) {
		template.convertAndSend("/aaa/update/" + message.getSender(),repos.getMessage());
	}
	
	@MessageMapping("/chat")
	@SendTo("/aa")
	public Message chat(@Payload Message message) {
		repos.addMessage(message);
		message.setMessage(message.getSender() + ": " + message.getMessage());
		return message;
	}
	
	@EventListener
	public void disconnect(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		template.convertAndSend("/aa", new Message() {{setSender((String) headerAccessor.getSessionAttributes().get("username")); setMessage(getSender() + " has left.");}});
	}
}
