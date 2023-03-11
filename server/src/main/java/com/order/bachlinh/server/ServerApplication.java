package com.order.bachlinh.server;

import com.order.bachlinh.server.boot.H3Server;
import com.order.bachlinh.server.boot.ServerInitializer;

public class ServerApplication {
    private static H3Server server;

    static {
        ServerInitializer initializer = new ServerInitializer();
        server = initializer.configServer();
    }

    public static void main(String[] args) throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.close()));
        server.run();
    }
}