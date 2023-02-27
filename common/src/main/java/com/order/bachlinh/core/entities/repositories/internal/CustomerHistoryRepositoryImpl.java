package com.order.bachlinh.core.entities.repositories.internal;

import com.order.bachlinh.core.entities.model.Customer;
import com.order.bachlinh.core.entities.model.CustomerHistory;
import com.order.bachlinh.core.entities.model.CustomerHistory_;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.entities.repositories.AbstractRepository;
import com.order.bachlinh.core.entities.repositories.CustomerHistoryRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Collection;

@Repository
@Log4j2
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Primary
class CustomerHistoryRepositoryImpl extends AbstractRepository<CustomerHistory, Integer> implements CustomerHistoryRepository {

    @Autowired
    CustomerHistoryRepositoryImpl(ApplicationContext applicationContext) {
        super(CustomerHistory.class, applicationContext.getBean(EntityManager.class), applicationContext.getBean(SessionFactory.class), applicationContext.getBean(EntityFactory.class));
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public CustomerHistory saveCustomerHistory(CustomerHistory customerHistory) {
        return this.save(customerHistory);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.MANDATORY)
    public boolean deleteCustomerHistory(CustomerHistory customerHistory) {
        Specification<CustomerHistory> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(CustomerHistory_.id), customerHistory.getId()));
        return this.delete(spec) == 1;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean deleteAll(Collection<CustomerHistory> histories) {
        try {
            this.deleteAllInBatch(histories);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Collection<CustomerHistory> getHistories(Customer customer) {
        Specification<CustomerHistory> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(CustomerHistory_.customerId), customer.getId()));
        return findAll(spec);
    }

    @Override
    public Collection<CustomerHistory> getHistoriesExpireNow(Date now) {
        Specification<CustomerHistory> spec = Specification.where(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(CustomerHistory_.removeDate), now)));
        return findAll(spec);
    }
}
