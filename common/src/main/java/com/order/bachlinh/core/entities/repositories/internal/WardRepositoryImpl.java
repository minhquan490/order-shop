package com.order.bachlinh.core.entities.repositories.internal;

import com.order.bachlinh.core.entities.model.Ward;
import com.order.bachlinh.core.entities.repositories.WardRepository;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.entities.repositories.AbstractRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Primary
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WardRepositoryImpl extends AbstractRepository<Ward, Integer> implements WardRepository {

    @Autowired
    WardRepositoryImpl(ApplicationContext context) {
        super(Ward.class, context.getBean(EntityManager.class), context.getBean(SessionFactory.class), context.getBean(EntityFactory.class));
    }

    @Override
    public boolean saveAllWard(Collection<Ward> wards) {
        return !saveAllAndFlush(wards).isEmpty();
    }

    @Override
    public long countAllWards() {
        return count();
    }

    @Override
    public Collection<Ward> getAllWards() {
        return findAll();
    }
}
