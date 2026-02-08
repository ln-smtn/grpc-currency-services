package com.example.currency_rate_provider.provider;

import com.example.currency.CurrencyRateProviderGrpc;
import com.example.currency.Empty;
import com.example.currency.RateResponse;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@GrpcService
public class CurrencyRateProviderGrpcService
        extends CurrencyRateProviderGrpc.CurrencyRateProviderImplBase {

    private static final double BASE = 92.50;     // базовый курс
    private static final double MAX_JITTER = 1.20; // +/- рандом

    @Override
    public void getUsdRubRate(Empty request,
                              io.grpc.stub.StreamObserver<RateResponse> responseObserver) {

        double jitter = ThreadLocalRandom.current().nextDouble(-MAX_JITTER, MAX_JITTER);
        double rate = round2(BASE + jitter);

        RateResponse response = RateResponse.newBuilder()
                .setPair("USDRUB")
                .setRate(rate)
                .setTimestampEpochMs(Instant.now().toEpochMilli())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
