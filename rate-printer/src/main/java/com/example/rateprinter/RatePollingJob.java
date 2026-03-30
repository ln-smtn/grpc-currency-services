package com.example.rateprinter;

import com.example.currency.CurrencyRateProviderGrpc;
import com.example.currency.Empty;
import com.example.currency.RateResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RatePollingJob {

    private static final Logger log = LoggerFactory.getLogger(RatePollingJob.class);

    @GrpcClient("currencyRateProvider")
    private CurrencyRateProviderGrpc.CurrencyRateProviderBlockingStub stub;

    private final Counter requestCounter;
    private final Counter errorCounter;
    private final Timer requestTimer;

    public RatePollingJob(MeterRegistry registry) {
        this.requestCounter = Counter.builder("grpc_client_requests_total")
                .description("Total gRPC client requests")
                .tag("method", "GetUsdRubRate")
                .register(registry);

        this.errorCounter = Counter.builder("grpc_client_errors_total")
                .description("Total gRPC client errors")
                .tag("method", "GetUsdRubRate")
                .register(registry);

        this.requestTimer = Timer.builder("grpc_client_request_duration")
                .description("gRPC client request duration")
                .tag("method", "GetUsdRubRate")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);
    }

    @Scheduled(fixedDelay = 5000)
    public void poll() {
        requestCounter.increment();
        log.info("gRPC REQUEST: GetUsdRubRate sending to server");

        requestTimer.record(() -> {
            try {
                RateResponse resp = stub.getUsdRubRate(Empty.newBuilder().build());

                log.info("gRPC RESPONSE: pair={}, rate={}, serverTs={}",
                        resp.getPair(), resp.getRate(),
                        Instant.ofEpochMilli(resp.getTimestampEpochMs()));
            } catch (Exception e) {
                errorCounter.increment();
                log.error("gRPC ERROR: {}", e.getMessage());
            }
        });
    }
}