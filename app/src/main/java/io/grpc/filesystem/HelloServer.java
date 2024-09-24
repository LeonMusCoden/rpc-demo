package io.grpc.filesystem;

import io.grpc.proto.HelloRequest;
import io.grpc.proto.HelloResponse;
import io.grpc.proto.HelloServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class HelloServer {
  public static void main(String[] args)
      throws IOException, InterruptedException {
    Server server =
        ServerBuilder.forPort(8090).addService(new HelloServiceImpl()).build();

    System.out.println("Starting gRPC Server...");
    server.start();
    System.out.println("Server started at " + server.getPort());

    server.awaitTermination();
  }

  static class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
    @Override
    public void sayHello(HelloRequest request,
                         StreamObserver<HelloResponse> responseObserver) {
      System.out.println("Received request: " + request.getName());
      String greeting = "Hello, " + request.getName();
      HelloResponse response =
          HelloResponse.newBuilder().setGreeting(greeting).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }
}
