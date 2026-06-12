package com.example.currency_rate_provider.controller;

import com.example.currency_rate_provider.service.CurrencyRateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CurrencyRateController {

    private final CurrencyRateService rateService;

    public CurrencyRateController(CurrencyRateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping("/api/rate/usdrub")
    public Map<String, Object> getUsdRubRate() {
        return Map.of(
                "pair", rateService.getPair(),
                "rate", rateService.getRate(),
                "timestampEpochMs", rateService.getTimestamp()
        );
    }
}