package com.order.bachlinh.core.component.setup.internal;

import com.order.bachlinh.core.component.setup.spi.SetupManager;
import com.order.bachlinh.core.component.setup.spi.SetupManagerFactory;
import org.springframework.context.ApplicationContext;

class DefaultSetupManagerFactory implements SetupManagerFactory {
    private final ApplicationContext applicationContext;

    DefaultSetupManagerFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public SetupManager buildManager() {
        return new DefaultSetupManager(applicationContext);
    }
}
