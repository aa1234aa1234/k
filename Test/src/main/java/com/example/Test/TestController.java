package com.example.Test;

import java.net.URI;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
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
	@SendTo("/aaa/broadcast/#")
	public Message join(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
		log.info("join: " + message.getSender());
		message.setMessage(message.getSender() + " has joined.");
		repos.addMessage(message);
		for(int i = 0; i<repos.getMessagecount(); i++) {
			log.info(repos.getMessage()[i].getMessage());
		}
		log.info("");
		
		return message;
	}
	
	@MessageMapping("/chat/image/download")
	public ResponseEntity<Resource> download(byte[] data) {
		ByteArrayResource resource = new ByteArrayResource(data);
	    return ResponseEntity.ok()
	            .contentType(MediaType.IMAGE_PNG)
	            .contentLength(resource.contentLength())
	            .header(HttpHeaders.CONTENT_DISPOSITION,
	                    ContentDisposition.attachment()
	                        .filename("hi")
	                        .build().toString())
	            .body(resource);
	}
	
//	@MessageMapping("/chat/image")
//	@SendTo("/aa")
//	public Message sendimage(@Payload Message data) {
//		repos.addMessage(data);
//		data.setMessage(data.getSender() + ": " + data.getMessage());
//		return data;
//	}
	
	@MessageMapping("/chat/join/update")
	public void update(@Payload Message message) {
		log.info("update: " + message.getSender());
		ArrayList<Message> list = new ArrayList<>();
		for(int i = 0; i<repos.getMessagecount(); i++) {
			list.add(repos.getMessage()[i]);
		}
		template.convertAndSend("/aaa/update/" + message.getSender(),list.toArray());
	}
	
	@MessageMapping("/testinghaha")
	@ResponseBody
	public ResponseEntity<String> create() {
	    return ResponseEntity.status(HttpStatus.CREATED)
	       .contentType(MediaType.TEXT_PLAIN)
	       .body("Custom string answer");
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
