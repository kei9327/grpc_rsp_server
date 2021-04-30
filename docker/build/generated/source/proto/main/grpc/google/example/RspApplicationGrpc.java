package google.example;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.20.0)",
    comments = "Source: google/example/game.proto")
public final class RspApplicationGrpc {

  private RspApplicationGrpc() {}

  public static final String SERVICE_NAME = "google.example.RspApplication";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<google.example.Gamer,
      google.example.Welecom> getLoginMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Login",
      requestType = google.example.Gamer.class,
      responseType = google.example.Welecom.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<google.example.Gamer,
      google.example.Welecom> getLoginMethod() {
    io.grpc.MethodDescriptor<google.example.Gamer, google.example.Welecom> getLoginMethod;
    if ((getLoginMethod = RspApplicationGrpc.getLoginMethod) == null) {
      synchronized (RspApplicationGrpc.class) {
        if ((getLoginMethod = RspApplicationGrpc.getLoginMethod) == null) {
          RspApplicationGrpc.getLoginMethod = getLoginMethod = 
              io.grpc.MethodDescriptor.<google.example.Gamer, google.example.Welecom>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "google.example.RspApplication", "Login"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  google.example.Gamer.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  google.example.Welecom.getDefaultInstance()))
                  .setSchemaDescriptor(new RspApplicationMethodDescriptorSupplier("Login"))
                  .build();
          }
        }
     }
     return getLoginMethod;
  }

  private static volatile io.grpc.MethodDescriptor<google.example.GRequest,
      google.example.GResponse> getGameMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Game",
      requestType = google.example.GRequest.class,
      responseType = google.example.GResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<google.example.GRequest,
      google.example.GResponse> getGameMethod() {
    io.grpc.MethodDescriptor<google.example.GRequest, google.example.GResponse> getGameMethod;
    if ((getGameMethod = RspApplicationGrpc.getGameMethod) == null) {
      synchronized (RspApplicationGrpc.class) {
        if ((getGameMethod = RspApplicationGrpc.getGameMethod) == null) {
          RspApplicationGrpc.getGameMethod = getGameMethod = 
              io.grpc.MethodDescriptor.<google.example.GRequest, google.example.GResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "google.example.RspApplication", "Game"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  google.example.GRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  google.example.GResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new RspApplicationMethodDescriptorSupplier("Game"))
                  .build();
          }
        }
     }
     return getGameMethod;
  }

  private static volatile io.grpc.MethodDescriptor<google.example.Gamer,
      google.example.RankList> getRankMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Rank",
      requestType = google.example.Gamer.class,
      responseType = google.example.RankList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<google.example.Gamer,
      google.example.RankList> getRankMethod() {
    io.grpc.MethodDescriptor<google.example.Gamer, google.example.RankList> getRankMethod;
    if ((getRankMethod = RspApplicationGrpc.getRankMethod) == null) {
      synchronized (RspApplicationGrpc.class) {
        if ((getRankMethod = RspApplicationGrpc.getRankMethod) == null) {
          RspApplicationGrpc.getRankMethod = getRankMethod = 
              io.grpc.MethodDescriptor.<google.example.Gamer, google.example.RankList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "google.example.RspApplication", "Rank"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  google.example.Gamer.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  google.example.RankList.getDefaultInstance()))
                  .setSchemaDescriptor(new RspApplicationMethodDescriptorSupplier("Rank"))
                  .build();
          }
        }
     }
     return getRankMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RspApplicationStub newStub(io.grpc.Channel channel) {
    return new RspApplicationStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RspApplicationBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RspApplicationBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RspApplicationFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RspApplicationFutureStub(channel);
  }

  /**
   */
  public static abstract class RspApplicationImplBase implements io.grpc.BindableService {

    /**
     */
    public void login(google.example.Gamer request,
        io.grpc.stub.StreamObserver<google.example.Welecom> responseObserver) {
      asyncUnimplementedUnaryCall(getLoginMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<google.example.GRequest> game(
        io.grpc.stub.StreamObserver<google.example.GResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(getGameMethod(), responseObserver);
    }

    /**
     */
    public void rank(google.example.Gamer request,
        io.grpc.stub.StreamObserver<google.example.RankList> responseObserver) {
      asyncUnimplementedUnaryCall(getRankMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getLoginMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                google.example.Gamer,
                google.example.Welecom>(
                  this, METHODID_LOGIN)))
          .addMethod(
            getGameMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                google.example.GRequest,
                google.example.GResponse>(
                  this, METHODID_GAME)))
          .addMethod(
            getRankMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                google.example.Gamer,
                google.example.RankList>(
                  this, METHODID_RANK)))
          .build();
    }
  }

  /**
   */
  public static final class RspApplicationStub extends io.grpc.stub.AbstractStub<RspApplicationStub> {
    private RspApplicationStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RspApplicationStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RspApplicationStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RspApplicationStub(channel, callOptions);
    }

    /**
     */
    public void login(google.example.Gamer request,
        io.grpc.stub.StreamObserver<google.example.Welecom> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<google.example.GRequest> game(
        io.grpc.stub.StreamObserver<google.example.GResponse> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getGameMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public void rank(google.example.Gamer request,
        io.grpc.stub.StreamObserver<google.example.RankList> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRankMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RspApplicationBlockingStub extends io.grpc.stub.AbstractStub<RspApplicationBlockingStub> {
    private RspApplicationBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RspApplicationBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RspApplicationBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RspApplicationBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<google.example.Welecom> login(
        google.example.Gamer request) {
      return blockingServerStreamingCall(
          getChannel(), getLoginMethod(), getCallOptions(), request);
    }

    /**
     */
    public google.example.RankList rank(google.example.Gamer request) {
      return blockingUnaryCall(
          getChannel(), getRankMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RspApplicationFutureStub extends io.grpc.stub.AbstractStub<RspApplicationFutureStub> {
    private RspApplicationFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RspApplicationFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RspApplicationFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RspApplicationFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<google.example.RankList> rank(
        google.example.Gamer request) {
      return futureUnaryCall(
          getChannel().newCall(getRankMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_LOGIN = 0;
  private static final int METHODID_RANK = 1;
  private static final int METHODID_GAME = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RspApplicationImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RspApplicationImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LOGIN:
          serviceImpl.login((google.example.Gamer) request,
              (io.grpc.stub.StreamObserver<google.example.Welecom>) responseObserver);
          break;
        case METHODID_RANK:
          serviceImpl.rank((google.example.Gamer) request,
              (io.grpc.stub.StreamObserver<google.example.RankList>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GAME:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.game(
              (io.grpc.stub.StreamObserver<google.example.GResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RspApplicationBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RspApplicationBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return google.example.Game.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RspApplication");
    }
  }

  private static final class RspApplicationFileDescriptorSupplier
      extends RspApplicationBaseDescriptorSupplier {
    RspApplicationFileDescriptorSupplier() {}
  }

  private static final class RspApplicationMethodDescriptorSupplier
      extends RspApplicationBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RspApplicationMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RspApplicationGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RspApplicationFileDescriptorSupplier())
              .addMethod(getLoginMethod())
              .addMethod(getGameMethod())
              .addMethod(getRankMethod())
              .build();
        }
      }
    }
    return result;
  }
}
