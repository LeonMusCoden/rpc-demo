package io.grpc.filesystem;

import io.grpc.proto.HelloRequest;
import io.grpc.proto.HelloResponse;
import io.grpc.proto.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class HelloClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("leonmuscat.de", 8090)
            .usePlaintext()
            .build();

        HelloServiceGrpc.HelloServiceBlockingStub stub = HelloServiceGrpc.newBlockingStub(channel);

        HelloRequest request = HelloRequest.newBuilder().setName("Leon").build();
        HelloResponse response = stub.sayHello(request);

        System.out.println("Response from server: " + response.getGreeting());

        channel.shutdown();
    }
}
