package com.order.bachlinh.core.component.setup.spi;

import org.springframework.context.ApplicationContext;

public abstract class AbstractSetup implements Setup {
    private ApplicationContext applicationContext;

    protected AbstractSetup(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
