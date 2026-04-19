package com.example.currency_rate_provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class CurrencyRateController {

    private final double base;
    private final double maxJitter;

    public CurrencyRateController(
            @org.springframework.beans.factory.annotation.Value("${currency.rate.base:92.50}") double base,
            @org.springframework.beans.factory.annotation.Value("${currency.rate.max-jitter:1.20}") double maxJitter) {
        this.base = base;
        this.maxJitter = maxJitter;
    }

    @GetMapping("/api/rate/usdrub")
    public Map<String, Object> getUsdRubRate() {
        double jitter = ThreadLocalRandom.current().nextDouble(-maxJitter, maxJitter);
        double rate = Math.round((base + jitter) * 100.0) / 100.0;

        return Map.of(
                "pair", "USDRUB",
                "rate", rate,
                "timestampEpochMs", Instant.now().toEpochMilli()
        );
    }
}