package com.example.gateway.grpc;

import com.example.grpc.EventServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Value("${grpc.domain-service.host}")
    private String grpcHost;

    @Value("${grpc.domain-service.port}")
    private int grpcPort;

    @Bean
    public EventServiceGrpc.EventServiceBlockingStub eventServiceBlockingStub() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
        return EventServiceGrpc.newBlockingStub(channel);
    }
}
