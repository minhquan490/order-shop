package com.order.bachlinh.core.configuration;

import com.order.bachlinh.core.entities.spi.SpringCacheManager;
import com.order.bachlinh.core.util.EntityUtils;
import jakarta.persistence.Cacheable;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Internal configuration for spring cache abstraction. This file
 * also defines how hibernate l2 caching implemented to this project.
 *
 * @author Hoang Minh Quan
 * */
@Configuration
@Log4j2
@EnableCaching(proxyTargetClass = true, mode = AdviceMode.ASPECTJ)
@RequiredArgsConstructor
@Lazy
public class CacheConfiguration {
    private final ApplicationContext applicationContext;

    @Bean
    @Lazy
    SpringCacheManager cacheManager() {
        SpringCacheManager cacheManager = new SpringCacheManager(initCacheManager());
        cacheManager.setAllowNullValues(false);
        cacheManager.setTransactionAware(true);
        cacheManager.setDefaultConfig(defaultConfiguration());
        return cacheManager;
    }

    private Collection<Class<?>> queryCacheStorage() {
        try {
            return EntityUtils.scanPackageEntity(applicationContext)
                    .stream()
                    .filter(this::conditionFilter)
                    .toList();
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private MutableConfiguration<Object, Object> defaultConfiguration() {
        return new MutableConfiguration<>()
                .setTypes(Object.class, Object.class)
                .setStoreByValue(true)
                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_HOUR));
    }

    private CacheManager initCacheManager() {
        return Optional.of(Caching.getCachingProvider().getCacheManager()).map(cacheManager -> {
            queryCacheStorage().forEach(cache -> {
                if (cache.isAnnotationPresent(Cacheable.class)) {
                    Cache cacheStorage = cache.getAnnotation(Cache.class);
                    if (cacheManager.getCache(cacheStorage.region()) == null) {
                        log.info("Create cache storage {}", cacheStorage.region());
                        cacheManager.createCache(cacheStorage.region(), defaultConfiguration());
                        log.info("Create done");
                    }
                }
            });
            return cacheManager;
        }).orElseThrow();
    }

    private boolean conditionFilter(Class<?> entity) {
        return entity.isAnnotationPresent(Cacheable.class) &&
                entity.isAnnotationPresent(Cache.class);
    }
}
