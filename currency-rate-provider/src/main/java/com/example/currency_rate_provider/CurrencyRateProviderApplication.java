package com.example.currency_rate_provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CurrencyRateProviderApplication {

    private static final Logger log = LoggerFactory.getLogger(CurrencyRateProviderApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CurrencyRateProviderApplication.class, args);
    }

    @Bean
    CommandLineRunner logVersion(@Value("${app.version}") String version) {
        return args -> log.info("currency-rate-provider version {} started successfully", version);
    }
}