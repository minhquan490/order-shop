package com.order.bachlinh.server.boot.spi;

import io.netty.channel.EventLoopGroup;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

public interface EventLoopGroupFactory {
    EventLoopGroup buildNioEventLoopGroup();
    EventLoopGroup buildSchedulerEventLoopGroup();

    interface Builder {
        Builder numberOfThread(int numberOfThread);
        Builder threadFactory(ThreadFactory threadFactory);
        Builder executor(Executor executor);
        EventLoopGroupFactory build();
    }
}
