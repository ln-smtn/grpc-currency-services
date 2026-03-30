package com.example.currency_rate_provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CurrencyRateProviderApplication {

    private static final Logger log = LoggerFactory.getLogger(CurrencyRateProviderApplication.class);

    public static void main(String[] args) {
        log.info("Starting currency-rate-provider version 1.0.0");
        SpringApplication.run(CurrencyRateProviderApplication.class, args);
        log.info("currency-rate-provider version 1.0.0 started successfully");
    }
}