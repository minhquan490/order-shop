package com.order.bachlinh.core.entities.spi.internal;

import com.order.bachlinh.core.entities.spi.EntityFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class EntityFactoryBuilderProvider {

    public static EntityFactory.EntityFactoryBuilder useDefaultEntityFactoryBuilder() {
        return new DefaultEntityFactory.DefaultEntityFactoryBuilder();
    }
}
