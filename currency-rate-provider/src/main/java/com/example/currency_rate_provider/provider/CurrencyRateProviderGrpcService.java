package com.example.currency_rate_provider.provider;

import com.example.currency.CurrencyRateProviderGrpc;
import com.example.currency.Empty;
import com.example.currency.RateResponse;
import com.example.currency_rate_provider.service.CurrencyRateService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class CurrencyRateProviderGrpcService
        extends CurrencyRateProviderGrpc.CurrencyRateProviderImplBase {

    private static final Logger log = LoggerFactory.getLogger(CurrencyRateProviderGrpcService.class);

    private final CurrencyRateService rateService;
    private final Counter requestCounter;
    private final Counter errorCounter;
    private final Timer requestTimer;

    public CurrencyRateProviderGrpcService(CurrencyRateService rateService, MeterRegistry registry) {
        this.rateService = rateService;

        this.requestCounter = Counter.builder("grpc_requests_total")
                .description("Total gRPC requests")
                .tag("method", "GetUsdRubRate")
                .register(registry);

        this.errorCounter = Counter.builder("grpc_errors_total")
                .description("Total gRPC 500 errors")
                .tag("method", "GetUsdRubRate")
                .register(registry);

        this.requestTimer = Timer.builder("grpc_request_duration")
                .description("gRPC request duration")
                .tag("method", "GetUsdRubRate")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);
    }

    @Override
    public void getUsdRubRate(Empty request,
                              io.grpc.stub.StreamObserver<RateResponse> responseObserver) {

        requestCounter.increment();
        log.info("gRPC REQUEST: GetUsdRubRate from client");

        requestTimer.record(() -> {
            try {
                double rate = rateService.getRate();
                String pair = rateService.getPair();
                long timestamp = rateService.getTimestamp();

                RateResponse response = RateResponse.newBuilder()
                        .setPair(pair)
                        .setRate(rate)
                        .setTimestampEpochMs(timestamp)
                        .build();

                log.info("gRPC RESPONSE: pair={}, rate={}, timestamp={}", pair, rate, timestamp);

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } catch (Exception e) {
                errorCounter.increment();
                log.error("gRPC ERROR in GetUsdRubRate: {}", e.getMessage(), e);
                responseObserver.onError(
                        io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
            }
        });
    }
}