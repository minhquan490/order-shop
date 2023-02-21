package com.order.bachlinh.web.repositories.internal;

import com.order.bachlinh.core.entities.model.Order;
import com.order.bachlinh.core.entities.model.Order_;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.repositories.AbstractRepository;
import com.order.bachlinh.web.repositories.spi.OrderRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
class OrderRepositoryImpl extends AbstractRepository<Order, String> implements OrderRepository {

    @Autowired
    OrderRepositoryImpl(ApplicationContext applicationContext) {
        super(Order.class, applicationContext.getBean(EntityManager.class), applicationContext.getBean(SessionFactory.class), applicationContext.getBean(EntityFactory.class));
    }

    @Override
    @Transactional(propagation = MANDATORY)
    public Order saveOrder(Order order) {
        return Optional.of(this.save(order)).orElse(null);
    }

    @Override
    @Transactional(propagation = MANDATORY, isolation = READ_COMMITTED)
    public Order updateOrder(Order order) {
        return saveOrder(order);
    }

    @Override
    @Transactional(propagation = MANDATORY, isolation = READ_COMMITTED)
    public boolean deleteOrder(Order order) {
        Specification<Order> spec = Specification.where(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Order_.ID), order.getId())));
        return this.delete(spec) == 1;
    }
}
