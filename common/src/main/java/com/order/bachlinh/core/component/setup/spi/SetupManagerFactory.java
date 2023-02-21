package com.order.bachlinh.core.component.setup.spi;

import org.springframework.context.ApplicationContext;

public interface SetupManagerFactory {
    SetupManager buildManager();

    interface Builder {
        Builder applicationContext(ApplicationContext context);
        SetupManagerFactory build();
    }
}
