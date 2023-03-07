package com.order.bachlinh.core.entities.spi;

import jakarta.persistence.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Entity scanner for scan entity package to find all class
 * marks by {@link jakarta.persistence.Entity} annotation.
 *
 * @author Hoang Minh Quan
 * */
public class ApplicationEntityScanner {
    private final List<String> basePackages;
    private final ApplicationContext context;
    public ApplicationEntityScanner(ApplicationContext context, List<String> basePackages) {
        this.basePackages = basePackages;
        this.context = context;
    }

    /**
     * Scan entity package to find all classes in it.
     *
     * @return set entity class.
     * */
    public final Set<Class<?>> scan() throws ClassNotFoundException {
        List<String> packages = basePackages;
        if (packages.isEmpty()) {
            return Collections.emptySet();
        }
        ClassPathScanningCandidateComponentProvider scanner = createClassPathScanningCandidateComponentProvider(
                this.context);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        Set<Class<?>> entitySet = new HashSet<>();
        for (String basePackage : packages) {
            if (StringUtils.hasText(basePackage)) {
                for (BeanDefinition candidate : scanner.findCandidateComponents(basePackage)) {
                    entitySet.add(ClassUtils.forName(Objects.requireNonNull(candidate.getBeanClassName()), this.context.getClassLoader()));
                }
            }
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
