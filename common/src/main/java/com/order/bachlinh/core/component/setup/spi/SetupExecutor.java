package com.order.bachlinh.core.component.setup.spi;

import com.order.bachlinh.core.component.setup.internal.SetupManagerFactoryProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Order(Ordered.HIGHEST_PRECEDENCE)
@Log4j2
public class SetupExecutor implements ApplicationListener<ContextRefreshedEvent> {
    private final SetupManager manager;

    public SetupExecutor(ApplicationContext context) {
        this.manager = buildSetupManager(context);
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        try {
            log.info("Start custom setup");
            manager.run();
            log.info("Finish setup");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Can not setup application", e);
        } finally {
            manager.close();
        }
    }

    private SetupManager buildSetupManager(ApplicationContext context) {
        SetupManagerFactoryProvider provider = new SetupManagerFactoryProvider();
        SetupManagerFactory.Builder builder = provider.provideBuilder();
        SetupManagerFactory setupManagerFactory = builder.applicationContext(context).build();
        return setupManagerFactory.buildManager();
    }
}
