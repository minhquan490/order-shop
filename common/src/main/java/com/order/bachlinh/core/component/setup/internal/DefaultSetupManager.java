package com.order.bachlinh.core.component.setup.internal;

import com.order.bachlinh.core.component.setup.spi.AbstractSetup;
import com.order.bachlinh.core.component.setup.spi.Setup;
import com.order.bachlinh.core.component.setup.spi.SetupManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.objenesis.SpringObjenesis;
import org.springframework.util.ClassUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Log4j2
class DefaultSetupManager implements SetupManager {

    private ApplicationContext applicationContext;
    private final Set<Setup> setups;

    DefaultSetupManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.setups = new HashSet<>();
    }

    @Override
    public boolean isClose() {
        return applicationContext == null && setups.isEmpty();
    }

    @Override
    public Collection<Setup> loadSetup() throws ClassNotFoundException {
        if (setups.isEmpty()) {
            SpringObjenesis springObjenesis = new SpringObjenesis();
            scan().forEach(setupClass -> {
                try {
                    log.info("Init setup for [{}]", setupClass.getName());
                    AbstractSetup setup = (AbstractSetup) springObjenesis.newInstance(setupClass);
                    setup.setApplicationContext(applicationContext);
                    setups.add(setup);
                    log.info("Init complete");
                } catch (Exception e) {
                    throw new IllegalArgumentException("Can not instance [" + setupClass.getSimpleName() + "]", e);
                }
            });
        }
        return setups;
    }

    @Override
    public void run() throws ClassNotFoundException {
        Collection<Setup> forSetup = loadSetup();
        forSetup.forEach(Setup::beforeExecute);
        forSetup.forEach(Setup::execute);
        forSetup.forEach(Setup::afterExecute);
    }

    @Override
    public void close() {
        setups.clear();
        applicationContext = null;
    }

    @SuppressWarnings("unchecked")
    private Collection<Class<Setup>> scan() throws ClassNotFoundException {
        String basePackage = "com.order.bachlinh.core.component.setup.provide";
        ClassPathScanningCandidateComponentProvider scanner = createClassPathScanningCandidateComponentProvider(
                this.applicationContext);
        scanner.addIncludeFilter(new AssignableTypeFilter(Setup.class));
        Set<Class<Setup>> entitySet = new HashSet<>();
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        for (BeanDefinition candidate : scanner.findCandidateComponents(basePackage)) {
            entitySet.add((Class<Setup>) Objects.requireNonNull(classLoader).loadClass(candidate.getBeanClassName()));
        }
        return entitySet;
    }

    private ClassPathScanningCandidateComponentProvider createClassPathScanningCandidateComponentProvider(
            ApplicationContext context) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.setEnvironment(context.getEnvironment());
        scanner.setResourceLoader(context);
        return scanner;
    }
}
