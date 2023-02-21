package com.order.bachlinh.core.component.setup.spi;

public interface Setup {
    void beforeExecute();
    void execute();
    void afterExecute();
}
