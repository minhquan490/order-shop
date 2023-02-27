package com.order.bachlinh.core.entities.repositories.internal;

import com.order.bachlinh.core.entities.model.ProductMedia;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.entities.repositories.AbstractRepository;
import com.order.bachlinh.core.entities.repositories.ProductMediaRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Primary
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class ProductMediaRepositoryImpl extends AbstractRepository<ProductMedia, Integer> implements ProductMediaRepository {

    @Autowired
    ProductMediaRepositoryImpl(ApplicationContext applicationContext) {
        super(ProductMedia.class, applicationContext.getBean(EntityManager.class), applicationContext.getBean(SessionFactory.class), applicationContext.getBean(EntityFactory.class));
    }

    @Override
    public ProductMedia loadMedia(int id) {
        return findById(id).orElse(null);
    }
}
