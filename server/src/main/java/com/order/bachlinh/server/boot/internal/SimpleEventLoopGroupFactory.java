package com.order.bachlinh.server.boot.internal;

import com.order.bachlinh.server.boot.spi.EventLoopGroupFactory;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class SimpleEventLoopGroupFactory implements EventLoopGroupFactory {
    private final int numberOfThread;
    private final ThreadFactory threadFactory;
    private final Executor executor;
    @Override
    public EventLoopGroup buildNioEventLoopGroup() {
        return new NioEventLoopGroup(numberOfThread, threadFactory);
    }

    @Override
    public EventLoopGroup buildSchedulerEventLoopGroup() {
        return new DefaultEventLoopGroup(numberOfThread, executor);
    }

    static EventLoopGroupFactory.Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    private static class Builder implements EventLoopGroupFactory.Builder {
        private int numberOfThread;
        private ThreadFactory threadFactory;
        private Executor executor;

        @Override
        public EventLoopGroupFactory.Builder numberOfThread(int numberOfThread) {
            this.numberOfThread = numberOfThread;
            return this;
        }

        @Override
        public EventLoopGroupFactory.Builder threadFactory(ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            return this;
        }

        @Override
        public EventLoopGroupFactory.Builder executor(Executor executor) {
            this.executor = executor;
            return this;
        }

        @Override
        public EventLoopGroupFactory build() {
            return new SimpleEventLoopGroupFactory(numberOfThread, threadFactory, executor);
        }
    }
}
