package com.order.bachlinh.server.loader.spi;

public interface ServerPropertiesLoader {
    void loadDefault();
    void load(String filePropertiesName);
}
