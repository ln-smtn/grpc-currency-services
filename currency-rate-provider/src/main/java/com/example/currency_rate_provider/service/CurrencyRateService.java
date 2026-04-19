package com.example.currency_rate_provider.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CurrencyRateService {

    private final double base;
    private final double maxJitter;

    public CurrencyRateService(
            @Value("${currency.rate.base:92.50}") double base,
            @Value("${currency.rate.max-jitter:1.20}") double maxJitter) {
        this.base = base;
        this.maxJitter = maxJitter;
    }

    public double getRate() {
        double jitter = ThreadLocalRandom.current().nextDouble(-maxJitter, maxJitter);
        return Math.round((base + jitter) * 100.0) / 100.0;
    }

    public String getPair() {
        return "USDRUB";
    }

    public long getTimestamp() {
        return Instant.now().toEpochMilli();
    }
}