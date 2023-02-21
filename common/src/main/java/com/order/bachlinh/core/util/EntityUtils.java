package com.order.bachlinh.core.util;

import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.spi.ApplicationEntityScanner;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.List;

public final class EntityUtils {

    public static Collection<Class<?>> scanPackageEntity(ApplicationContext applicationContext) throws ClassNotFoundException {
        String packageName = BaseEntity.class.getPackage().getName();
        return new ApplicationEntityScanner(applicationContext, List.of(packageName)).scan();
    }

    private EntityUtils() {
        throw new UnsupportedOperationException("Can not instance ClassUtil object");
    }
}
