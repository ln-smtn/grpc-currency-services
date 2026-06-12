package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RatePrinterApplication {

    private static final Logger log = LoggerFactory.getLogger(RatePrinterApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RatePrinterApplication.class, args);
    }

    @Bean
    CommandLineRunner logVersion(@Value("${app.version}") String version) {
        return args -> log.info("rate-printer version {} started successfully", version);
    }
}