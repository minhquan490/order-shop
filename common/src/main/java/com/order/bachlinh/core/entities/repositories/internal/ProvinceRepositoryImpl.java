package com.order.bachlinh.core.entities.repositories.internal;

import com.order.bachlinh.core.entities.model.Province;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.entities.repositories.AbstractRepository;
import com.order.bachlinh.core.entities.repositories.ProvinceRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
@Primary
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class ProvinceRepositoryImpl extends AbstractRepository<Province, Integer> implements ProvinceRepository {

    @Autowired
    ProvinceRepositoryImpl(ApplicationContext context) {
        super(Province.class, context.getBean(EntityManager.class), context.getBean(SessionFactory.class), context.getBean(EntityFactory.class));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean saveAllProvinces(Collection<Province> provinces) {
        return !saveAllAndFlush(provinces).isEmpty();
    }

    @Override
    public long countProvince() {
        return count();
    }

    @Override
    public Collection<Province> getAllProvinces() {
        return findAll();
    }
}
