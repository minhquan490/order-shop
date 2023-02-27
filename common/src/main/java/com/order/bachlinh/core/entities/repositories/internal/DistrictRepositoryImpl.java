package com.order.bachlinh.core.entities.repositories.internal;

import com.order.bachlinh.core.entities.model.District;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.entities.repositories.AbstractRepository;
import com.order.bachlinh.core.entities.repositories.DistrictRepository;
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
public class DistrictRepositoryImpl extends AbstractRepository<District, Integer> implements DistrictRepository {

    @Autowired
    DistrictRepositoryImpl(ApplicationContext context) {
        super(District.class, context.getBean(EntityManager.class), context.getBean(SessionFactory.class), context.getBean(EntityFactory.class));
    }

    @Override
    public boolean saveAllDistrict(Collection<District> districts) {
        return !saveAllAndFlush(districts).isEmpty();
    }

    @Override
    public long countDistrict() {
        return count();
    }

    @Override
    public Collection<District> getAllDistrict() {
        return findAll();
    }
}
