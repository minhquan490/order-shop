package com.order.bachlinh.server.component.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.incubator.codec.http3.Http3DataFrame;
import io.netty.incubator.codec.http3.Http3FrameToHttpObjectCodec;
import io.netty.incubator.codec.http3.Http3HeadersFrame;
import io.netty.incubator.codec.http3.Http3RequestStreamInboundHandler;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.SocketAddress;

public final class FrontHandler extends Http3RequestStreamInboundHandler implements ChannelOutboundHandler {

    private final Http3FrameToHttpObjectCodec internalHandler;
    private static final MethodHandle channelReadHeader;
    private static final MethodHandle channelReadData;

    static {
        try {
            channelReadHeader = channelReadHeader();
            channelReadData = channelReadData();
        } catch (Throwable e) {
            throw new IllegalStateException("Can not extract protected method of Http3FrameToHttpObjectCodec", e);
        }
    }

    public FrontHandler() {
        this.internalHandler = new Http3FrameToHttpObjectCodec(true, true);
    }

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) {
        internalHandler.bind(ctx, localAddress, promise);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        internalHandler.connect(ctx, remoteAddress, localAddress, promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) {
        internalHandler.disconnect(ctx, promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) {
        internalHandler.close(ctx, promise);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) {
        internalHandler.deregister(ctx, promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        internalHandler.read(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        internalHandler.write(ctx, msg, promise);
        flush(ctx);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) {
        internalHandler.flush(ctx);
    }

    @Override
    protected void channelRead(ChannelHandlerContext ctx, Http3HeadersFrame frame, boolean isLast) {
        try {
            channelReadHeader.invokeExact(internalHandler, ctx, frame, isLast);
        } catch (Throwable e) {
            throw new IllegalStateException("Can not call method Http3FrameToHttpObjectCodec.channelRead(ChannelHandlerContext, Http3HeadersFrame, boolean)", e);
        }
    }

    @Override
    protected void channelRead(ChannelHandlerContext ctx, Http3DataFrame frame, boolean isLast) {
        try {
            channelReadData.invokeExact(internalHandler, ctx, frame, isLast);
        } catch (Throwable e) {
            throw new IllegalStateException("Can not call method Http3FrameToHttpObjectCodec.channelRead(ChannelHandlerContext, Http3DataFrame, boolean)", e);
        }
    }

    private static MethodHandle channelReadHeader() throws Throwable {
        MethodHandles.Lookup caller = MethodHandles.lookup();
        String invokedName = "apply";
        MethodType samMethodType = MethodType.methodType(void.class, ChannelHandlerContext.class, Http3HeadersFrame.class, boolean.class);
        MethodType invokedType = MethodType.methodType(ChannelHeaderRead.class, FrontHandler.class);
        MethodHandle implementMethod = caller.findVirtual(Http3FrameToHttpObjectCodec.class, "channelRead", samMethodType);
        CallSite site = LambdaMetafactory.metafactory(caller, invokedName, invokedType, samMethodType, implementMethod, samMethodType);
        return site.getTarget();
    }

    private static MethodHandle channelReadData() throws Throwable {
        MethodHandles.Lookup caller = MethodHandles.lookup();
        String invokedName = "apply";
        MethodType samMethodType = MethodType.methodType(void.class, ChannelHandlerContext.class, Http3DataFrame.class, boolean.class);
        MethodType invokedType = MethodType.methodType(ChannelDataRead.class, FrontHandler.class);
        MethodHandle implementMethod = caller.findVirtual(Http3FrameToHttpObjectCodec.class, "channelRead", samMethodType);
        CallSite site = LambdaMetafactory.metafactory(caller, invokedName, invokedType, samMethodType, implementMethod, samMethodType);
        return site.getTarget();
    }

    private interface ChannelHeaderRead {
        void apply(ChannelHandlerContext ctx, Http3HeadersFrame frame, boolean isLast);
    }

    private interface ChannelDataRead {
        void apply(ChannelHandlerContext ctx, Http3DataFrame frame, boolean isLast);
    }
}