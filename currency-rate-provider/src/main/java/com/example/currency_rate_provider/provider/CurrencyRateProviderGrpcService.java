package com.example.currency_rate_provider.provider;

import com.example.currency.CurrencyRateProviderGrpc;
import com.example.currency.Empty;
import com.example.currency.RateResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@GrpcService
public class CurrencyRateProviderGrpcService
        extends CurrencyRateProviderGrpc.CurrencyRateProviderImplBase {

    private static final Logger log = LoggerFactory.getLogger(CurrencyRateProviderGrpcService.class);

    private static final double BASE = 92.50;
    private static final double MAX_JITTER = 1.20;

    private final Counter requestCounter;
    private final Counter errorCounter;
    private final Timer requestTimer;

    public CurrencyRateProviderGrpcService(MeterRegistry registry) {
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
                double jitter = ThreadLocalRandom.current().nextDouble(-MAX_JITTER, MAX_JITTER);
                double rate = round2(BASE + jitter);

                RateResponse response = RateResponse.newBuilder()
                        .setPair("USDRUB")
                        .setRate(rate)
                        .setTimestampEpochMs(Instant.now().toEpochMilli())
                        .build();

                log.info("gRPC RESPONSE: pair={}, rate={}, timestamp={}",
                        response.getPair(), response.getRate(), response.getTimestampEpochMs());

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

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}