package io.grpc.filesystem;

import io.grpc.proto.HelloRequest;
import io.grpc.proto.HelloResponse;
import io.grpc.proto.StreamingServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StreamingServer {

    public static void main(String[] args)
            throws IOException, InterruptedException {
        Server server =
                ServerBuilder.forPort(8090).addService(new StreamingServiceImpl()).build();

        System.out.println("Starting gRPC Server...");
        server.start();
        System.out.println("Server started at " + server.getPort());

        server.awaitTermination();
    }

    static class StreamingServiceImpl extends StreamingServiceGrpc.StreamingServiceImplBase {
        public StreamObserver<HelloRequest> streamHello(final StreamObserver<HelloResponse> responseObserver) {
            return new StreamObserver<>() {
                final List<String> names = new ArrayList<>();

                @Override
                public void onNext(HelloRequest request) {
                    System.out.println("Received request: " + request.getName());
                    names.add(request.getName());
                }

                @Override
                public void onError(Throwable throwable) {
                    System.err.println("Error occurred: " + throwable);
                }

                @Override
                public void onCompleted() {
                    String greeting = "Hello " + String.join(", ", names) + "!";
                    HelloResponse response = HelloResponse.newBuilder().setGreeting(greeting).build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
