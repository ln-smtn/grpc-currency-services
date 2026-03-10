package com.example.currency_rate_provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class CurrencyRateController {

    private static final double BASE = 92.50;
    private static final double MAX_JITTER = 1.20;

    @GetMapping("/api/rate/usdrub")
    public Map<String, Object> getUsdRubRate() {
        double jitter = ThreadLocalRandom.current().nextDouble(-MAX_JITTER, MAX_JITTER);
        double rate = Math.round((BASE + jitter) * 100.0) / 100.0;

        return Map.of(
                "pair", "USDRUB",
                "rate", rate,
                "timestampEpochMs", Instant.now().toEpochMilli()
        );
    }
}