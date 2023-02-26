package com.order.bachlinh.core.entities.spi.trigger;

import com.order.bachlinh.core.annotation.Store;
import com.order.bachlinh.core.component.search.SearchEngine;
import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.model.Product;
import com.order.bachlinh.core.entities.spi.AbstractTrigger;
import com.order.bachlinh.core.entities.spi.TriggerMode;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class ProductTrigger extends AbstractTrigger {
    private final SearchEngine searchEngine;
    private final ThreadPoolTaskExecutor executor;

    public ProductTrigger(ApplicationContext applicationContext , TriggerMode mode) {
        super(applicationContext, mode);
        this.searchEngine = applicationContext.getBean(SearchEngine.class);
        this.executor = applicationContext.getBean(ThreadPoolTaskExecutor.class);
    }

    @Override
    public void execute(BaseEntity entity) {
        Runnable runnable = () -> doExecute(entity);
        executor.execute(runnable);
    }

    public void doExecute(BaseEntity entity) {
        Product product = (Product) entity;
        Class<?> productClass = product.getClass();
        if (!productClass.isAnnotationPresent(Store.class)) {
            return;
        }
        String storeName = productClass.getAnnotation(Store.class).name();
        searchEngine.analyze(storeName, product.getName(), product);
    }
}
