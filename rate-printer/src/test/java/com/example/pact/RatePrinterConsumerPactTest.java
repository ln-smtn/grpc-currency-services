package com.example.pact;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "currency-rate-provider", port = "8888")
public class RatePrinterConsumerPactTest {

    @Pact(consumer = "rate-printer")
    public V4Pact getUsdRubRatePact(PactDslWithProvider builder) {
        return builder
                .given("rate provider is available")
                .uponReceiving("a request for USDRUB rate")
                .path("/api/rate/usdrub")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(newJsonBody(body -> {
                    body.stringType("pair", "USDRUB");
                    body.numberType("rate", 92.5);
                    body.numberType("timestampEpochMs", 1234567890123L);
                }).build())
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "getUsdRubRatePact")
    void testGetUsdRubRate(MockServer mockServer) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(mockServer.getUrl() + "/api/rate/usdrub"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("USDRUB"));
        assertTrue(response.body().contains("rate"));
    }
}