package com.example.Test;

import java.io.BufferedReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.stream.Collectors;

import org.json.JSONObject;
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
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Controller
public class TestController {
	@Autowired
	
	private SimpMessagingTemplate template;
	@Autowired
	private UserRepository userRepos;
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
	
	@GetMapping("/")
	public String home() {
		return "redirect:/home";
	}
	
	@GetMapping("/home")
	public String talkinaway() {
		return "/index.html";
	}
	
	@GetMapping("/test")
	public String idontknow() {
		return "/a.html";
	}
	
	
	
	@PostMapping("/test")
	public String whatimtosayillsayitanyway(@RequestBody Message message, Model model) {
		model.addAttribute("sender",message.getSender());
		model.addAttribute("message",message.getMessage());
		return "/a.html";
//		return ResponseEntity.ok()
//				.contentType(MediaType.APPLICATION_JSON)
//				.body(message);
	}
	
	@PostMapping("/login_process")
	public String todayisanotherdaytofindyou() {
		return "redirect:/home";
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
	
	
	
//	@RequestMapping("/testing/bar")
//    public ResponseEntity<String> invalidcharacterfoundinmethodname() {
//		WebClient client = WebClient.builder()
//				.baseUrl("https://housevalue.co.kr/")
//				.defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36")
//				.build();
//		MultiValueMap<String, String> formdata = new LinkedMultiValueMap<>();
//		formdata.set("address1", "서울");
//		formdata.set("address2", "중구");
//		formdata.set("address3", "명동2가");
//		formdata.set("mountain_yn", "N");
//		formdata.set("main_address_no", "1");
//		formdata.set("sub_address_no", "18
//		formdata.set("longitude", "126.98586313967616");
//		formdata.set("latitude", "37.56342853082633");
//		formdata.set("d_search", "3bhdpV6xBvdTZeSTB3TH5gcadqImuKS3LDzw1c6OyOHNmUGVNh");
//		formdata.set("only_price", "false");
//		formdata.set("idx", "0");
//		formdata.set("view_this_real", "");
//		formdata.set("view_this_rental", "");
//		formdata.set("view_likeness_real", "");
//		formdata.set("view_likeness_rental", "");
//		formdata.set("view_likeness_real2", "");
//		formdata.set("view_likeness_real3", "");
//		formdata.set("view_contract_real", "");
//		formdata.set("view_close_real", "");
//		formdata.set("view_contract_rental", "");
//		formdata.set("view_close_rental", "");
//		formdata.set("view_contract_real2", "");
//		formdata.set("view_close_real2", "");
//		formdata.set("view_contract_real3", "");
//		formdata.set("view_close_real3", "");
//		WebClient.ResponseSpec response = client.post()
//		.uri("/h_data/jibun_detail/real_price")
//		.bodyValue(formdata)
//		.retrieve();
//		
//		System.out.println(client.toString());
//		JSONObject obj = new JSONObject(response.toEntity(String.class).block().getBody());
////		for(Map.Entry<String,Object> e : obj.toMap().entrySet()) {
////			System.out.println(e);
////		}
//		//System.out.println(obj.get("gunmul_result"));
//		return ResponseEntity.ok()
//				.body(obj.toString());
//    }
	
	
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
	
//	@MessageMapping("/testinghaha")
//	public ResponseEntity<String> create() {
//		WebClient a = WebClient.builder()
//				.baseUrl("http://localhost:8080/")
//				.build();
//		ResponseEntity<String> res = a.post()
//		.uri("/testing/bar")
//		.retrieve().toEntity(String.class).block();
//	    return ResponseEntity.status(HttpStatus.CREATED)
//	       .contentType(MediaType.TEXT_PLAIN)
//	       .body("Custom string answer");
//	}
	
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
