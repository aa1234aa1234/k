package com.test.test;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication a = new SpringApplication(TestApplication.class);
		a.setDefaultProperties(Collections.singletonMap("server.port", 9000));
		a.run(args);
	}

}
