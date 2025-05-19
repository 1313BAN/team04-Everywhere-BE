package com.ssafy.enjoytrip.everywhere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EverywhereApplication {

	public static void main(String[] args) {
		SpringApplication.run(EverywhereApplication.class, args);
	}

}
