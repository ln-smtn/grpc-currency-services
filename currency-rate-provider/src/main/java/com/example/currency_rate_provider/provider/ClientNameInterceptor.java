package com.example.currency_rate_provider.provider;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

@GrpcGlobalServerInterceptor
public class ClientNameInterceptor implements ServerInterceptor {

    public static final Metadata.Key<String> CLIENT_NAME_KEY =
            Metadata.Key.of("client-name", Metadata.ASCII_STRING_MARSHALLER);

    public static final Context.Key<String> CLIENT_NAME_CTX = Context.key("client-name");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String clientName = headers.get(CLIENT_NAME_KEY);
        if (clientName == null) {
            clientName = "unknown";
        }

        Context ctx = Context.current().withValue(CLIENT_NAME_CTX, clientName);
        return Contexts.interceptCall(ctx, call, headers, next);
    }
}
