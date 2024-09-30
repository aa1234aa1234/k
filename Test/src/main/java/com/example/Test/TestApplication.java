package com.example.Test;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@SpringBootApplication
@ComponentScan
public class TestApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
		//new TestClass();
	}

}


class TestClass {
	@Autowired
	public RestTemplate restTemplate = new RestTemplate();
	public TestClass() {
		WebClient client = WebClient.builder()
				.baseUrl("https://housevalue.co.kr/")
				.defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36")
				.build();
		MultiValueMap<String, String> formdata = new LinkedMultiValueMap<>();
		formdata.set("address1", "서울");
		formdata.set("address2", "중구");
		formdata.set("address3", "명동2가");
		formdata.set("mountain_yn", "N");
		formdata.set("main_address_no", "1");
		formdata.set("sub_address_no", "18");
		formdata.set("longitude", "126.98586313967616");
		formdata.set("latitude", "37.56342853082633");
		formdata.set("d_search", "3bhdpV6xBvdTZeSTB3TH5gcadqImuKS3LDzw1c6OyOHNmUGVNh");
		formdata.set("only_price", "false");
		formdata.set("idx", "0");
		formdata.set("view_this_real", "");
		formdata.set("view_this_rental", "");
		formdata.set("view_likeness_real", "");
		formdata.set("view_likeness_rental", "");
		formdata.set("view_likeness_real2", "");
		formdata.set("view_likeness_real3", "");
		formdata.set("view_contract_real", "");
		formdata.set("view_close_real", "");
		formdata.set("view_contract_rental", "");
		formdata.set("view_close_rental", "");
		formdata.set("view_contract_real2", "");
		formdata.set("view_close_real2", "");
		formdata.set("view_contract_real3", "");
		formdata.set("view_close_real3", "");
		WebClient.ResponseSpec response = client.post()
		.uri("/h_data/jibun_detail/real_price")
		.bodyValue(formdata)
		.retrieve();
		System.out.println(client.toString());
		JSONObject obj = new JSONObject(response.toEntity(String.class).block().getBody());
//		for(Map.Entry<String,Object> e : obj.toMap().entrySet()) {
//			System.out.println(e);
//		}
		System.out.println(obj.get("gunmul_result"));

	}
}