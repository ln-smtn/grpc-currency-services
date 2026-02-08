package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RatePrinterApplication {

	public static void main(String[] args) {

        SpringApplication.run(RatePrinterApplication.class, args);
	}

}
