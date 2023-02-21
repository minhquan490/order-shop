package com.order.bachlinh.core.repositories.internal;

import com.order.bachlinh.core.entities.model.Product;
import com.order.bachlinh.core.entities.model.Product_;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.repositories.AbstractRepository;
import com.order.bachlinh.core.repositories.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@Primary
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class ProductRepositoryImpl extends AbstractRepository<Product, String> implements ProductRepository {
    private static final String LIKE_PATTERN = "{0}%";

    @Autowired
    ProductRepositoryImpl(ApplicationContext applicationContext) {
        super(Product.class, applicationContext.getBean(EntityManager.class), applicationContext.getBean(SessionFactory.class), applicationContext.getBean(EntityFactory.class));
    }

    @Override
    @Transactional(propagation = MANDATORY)
    public Product saveProduct(Product product) {
        return this.save(product);
    }

    @Override
    @Transactional(propagation = MANDATORY, isolation = READ_COMMITTED)
    public Product updateProduct(Product product) {
        return this.saveProduct(product);
    }

    @Override
    @Transactional(propagation = MANDATORY)
    public boolean deleteProduct(Product product) {
        Specification<Product> spec = Specification.where(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Product_.ID), product.getId())));
        return this.delete(spec) == 1;
    }

    @Override
    public boolean productNameExist(Product product) {
        Specification<Product> spec = Specification.where(((root, query, criteriaBuilder) -> {
            query.select(root.get(Product_.ID));
            return criteriaBuilder.equal(root.get(Product_.NAME), product.getName());
        }));
        return get(spec) != null;
    }

    @Override
    public Product getProductByCondition(Map<String, Object> conditions) {
        Specification<Product> spec = specWithCondition(conditions);
        return get(spec);
    }

    @Override
    public Page<Product> getProductsByCondition(Map<String, Object> conditions, Pageable pageable) {
        Specification<Product> spec = specWithCondition(conditions);
        return findAll(spec, pageable);
    }

    private Specification<Product> specWithCondition(Map<String, Object> conditions) {
        Map<String, Object> copyConditions = conditions.entrySet()
                .stream()
                .filter(stringObjectEntry -> stringObjectEntry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Specification.where(((root, query, criteriaBuilder) -> {
            query.multiselect(
                    root.get(Product_.id),
                    root.get(Product_.carts),
                    root.get(Product_.size),
                    root.get(Product_.name),
                    root.get(Product_.color),
                    root.get(Product_.price),
                    root.get(Product_.taobaoUrl),
                    root.get(Product_.description),
                    root.get(Product_.categories),
                    root.get(Product_.pictures)
            );
            root.join(Product_.categories, JoinType.INNER);
            root.join(Product_.pictures, JoinType.LEFT);
            AtomicReference<Predicate> predicateWrapper = new AtomicReference<>();
            copyConditions.forEach((key, value) -> {
                Predicate predicate;
                if (key.equals(Product_.PRICE)) {
                    predicate = criteriaBuilder.lessThanOrEqualTo(root.get(key), (int) value);
                } else {
                    if (key.equals(Product_.NAME)) {
                        predicate = criteriaBuilder.like(root.get(Product_.NAME), MessageFormat.format(LIKE_PATTERN, value));
                    } else {
                        predicate = criteriaBuilder.equal(root.get(key), value);
                    }
                }
                predicateWrapper.set(criteriaBuilder.or(predicate));
            });
            return predicateWrapper.get();
        }));
    }
}
