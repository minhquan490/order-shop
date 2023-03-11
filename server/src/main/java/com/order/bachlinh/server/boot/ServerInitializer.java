package com.order.bachlinh.server.boot;

import com.order.bachlinh.server.loader.internal.ServerPropertiesLoaderProvider;
import io.netty.incubator.codec.quic.InsecureQuicTokenHandler;

public final class ServerInitializer {
    public H3Server configServer() {
        ServerPropertiesLoaderProvider.propertiesLoader().loadDefault();
        return H3Server.builder()
                .host(System.getProperty("server.host"))
                .port(Integer.parseInt(System.getProperty("server.port")))
                .certPath(System.getProperty("server.certificate.cert"))
                .keyPath(System.getProperty("server.certificate.key"))
                .password(System.getProperty("server.certificate.password"))
                .maxData(Long.parseLong(System.getProperty("server.initial.maxdata")))
                .maxStreamDataLocal(Long.parseLong(System.getProperty("server.initial.maxstream.local")))
                .maxStreamDataRemote(Long.parseLong(System.getProperty("server.initial.maxstream.remote")))
                .maxStream(Long.parseLong(System.getProperty("server.initial.maxstream")))
                .handlerPackage(System.getProperty("server.handler.package"))
                .numberOfThread(Integer.parseInt(System.getProperty("server.thread")))
                .tokenHandler(InsecureQuicTokenHandler.INSTANCE)
                .build();
    }
}
