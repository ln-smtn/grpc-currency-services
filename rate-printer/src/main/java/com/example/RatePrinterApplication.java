package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RatePrinterApplication {

    private static final Logger log = LoggerFactory.getLogger(RatePrinterApplication.class);

    public static void main(String[] args) {
        log.info("Starting rate-printer version 1.0.0");
        SpringApplication.run(RatePrinterApplication.class, args);
        log.info("rate-printer version 1.0.0 started successfully");
    }
}