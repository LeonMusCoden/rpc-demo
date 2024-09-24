package io.grpc.filesystem;

import io.grpc.proto.HelloRequest;
import io.grpc.proto.HelloResponse;
import io.grpc.proto.StreamingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class StreamingClient {

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("leonmuscat.de", 8090).usePlaintext().build();

        StreamingServiceGrpc.StreamingServiceStub stub = StreamingServiceGrpc.newStub(channel);

        StreamObserver<HelloResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(HelloResponse helloResponse) {
                System.out.println("Received response: " + helloResponse.getGreeting());
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error occurred: " + throwable);
            }

            @Override
            public void onCompleted() {
                System.out.println("Stream completed");
            }
        };

        StreamObserver<HelloRequest> requestObserver = stub.streamHello(responseObserver);

        requestObserver.onNext(HelloRequest.newBuilder().setName("David").build());
        requestObserver.onNext(HelloRequest.newBuilder().setName("Matteo").build());
        requestObserver.onNext(HelloRequest.newBuilder().setName("Felix").build());

        requestObserver.onCompleted();

        channel.awaitTermination(5, TimeUnit.SECONDS);
    }

}
