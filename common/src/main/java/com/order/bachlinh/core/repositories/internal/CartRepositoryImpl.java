package com.order.bachlinh.core.repositories.internal;

import com.order.bachlinh.core.entities.model.Cart;
import com.order.bachlinh.core.entities.model.Cart_;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.repositories.AbstractRepository;
import com.order.bachlinh.core.repositories.CartRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Primary
class CartRepositoryImpl extends AbstractRepository<Cart, String> implements CartRepository {

    @Autowired
    CartRepositoryImpl(ApplicationContext applicationContext) {
        super(Cart.class, applicationContext.getBean(EntityManager.class), applicationContext.getBean(SessionFactory.class), applicationContext.getBean(EntityFactory.class));
    }

    @Override
    @Transactional(propagation = MANDATORY)
    public Cart saveCart(Cart cart) {
        return Optional.of(this.save(cart)).orElse(null);
    }

    @Override
    @Transactional(propagation = MANDATORY, isolation = READ_COMMITTED)
    public Cart updateCart(Cart cart) {
        return saveCart(cart);
    }

    @Override
    public boolean deleteCart(Cart cart) {
        Specification<Cart> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Cart_.id), cart.getId()));
        return this.delete(spec) == 1;
    }
}
