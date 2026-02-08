package com.example.rateprinter;

import com.example.currency.CurrencyRateProviderGrpc;
import com.example.currency.Empty;
import com.example.currency.RateResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RatePollingJob {

    @GrpcClient("currencyRateProvider")
    private CurrencyRateProviderGrpc.CurrencyRateProviderBlockingStub stub;

    @Scheduled(fixedDelay = 5000)
    public void poll() {
        RateResponse resp = stub.getUsdRubRate(Empty.newBuilder().build());

        System.out.printf(
                "[%s] %s = %.2f (serverTs=%s)%n",
                Instant.now(),
                resp.getPair(),
                resp.getRate(),
                Instant.ofEpochMilli(resp.getTimestampEpochMs())
        );
    }
}
