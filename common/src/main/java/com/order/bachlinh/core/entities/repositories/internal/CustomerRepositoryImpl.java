package com.order.bachlinh.core.entities.repositories.internal;

import com.order.bachlinh.core.entities.model.Customer;
import com.order.bachlinh.core.entities.model.Customer_;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.entities.repositories.AbstractRepository;
import com.order.bachlinh.core.entities.repositories.CustomerRepository;
import com.order.bachlinh.core.entities.repositories.spi.Condition;
import com.order.bachlinh.core.entities.repositories.spi.ConditionExecutor;
import com.order.bachlinh.core.entities.repositories.spi.Join;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.JoinType;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@Primary
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class CustomerRepositoryImpl extends AbstractRepository<Customer, String> implements CustomerRepository {
    private final AtomicLong customerCount = new AtomicLong(0);

    @Autowired
    public CustomerRepositoryImpl(ApplicationContext applicationContext) {
        super(Customer.class, applicationContext.getBean(EntityManager.class), applicationContext.getBean(SessionFactory.class), applicationContext.getBean(EntityFactory.class));
    }

    @Override
    public Customer getCustomerById(String id) {
        Specification<Customer> spec = Specification.where(((root, query, criteriaBuilder) -> {
            root.join(Customer_.refreshToken, JoinType.INNER);
            return criteriaBuilder.equal(root.get(Customer_.ID), id);
        }));
        return get(spec);
    }

    @Override
    public Customer getCustomerByUsername(String username) {
        Specification<Customer> spec = Specification.where(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Customer_.USERNAME), username)));
        return get(spec);
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        Specification<Customer> spec = Specification.where(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Customer_.EMAIL), email)));
        return get(spec);
    }

    @Override
    public Customer getCustomerByPhone(String phone) {
        Specification<Customer> spec = Specification.where(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Customer_.PHONE_NUMBER), phone)));
        return get(spec);
    }

    @Override
    public Customer getCustomerUseJoin(Object customerId, Collection<Join> joins) {
        Specification<Customer> spec = Specification.where(((root, query, criteriaBuilder) -> {
            joins.forEach(join -> root.join(join.getAttribute(), join.getType()));
            return criteriaBuilder.equal(root.get("id"), customerId);
        }));
        return get(spec);
    }

    @Override
    public long countCustomer() {
        return customerCount.get();
    }

    @Override
    @Transactional(propagation = MANDATORY)
    public boolean deleteCustomer(@NonNull Customer customer) {
        if (StringUtils.hasText((CharSequence) customer.getId())) {
            long numRowDeleted = this.delete(Specification.where((root, query, builder) -> builder.equal(root.get(Customer_.ID), customer.getId())));
            customerCount.set((int) (customerCount.get() - numRowDeleted));
            return numRowDeleted == 1;
        } else {
            return false;
        }
    }

    @Override
    @Transactional(propagation = MANDATORY)
    public Customer saveCustomer(@NonNull Customer customer) {
        return this.save(customer);
    }

    @Override
    @Transactional(propagation = MANDATORY, isolation = READ_COMMITTED)
    public Customer updateCustomer(@NonNull Customer customer) {
        return this.saveCustomer(customer);
    }

    @Override
    public boolean usernameExist(String username) {
        return this.getCustomerByUsername(username) != null;
    }

    @Override
    public boolean phoneNumberExist(String phone) {
        return this.getCustomerByPhone(phone) != null;
    }

    @Override
    public boolean emailExist(String email) {
        return this.getCustomerByEmail(email) != null;
    }

    @Override
    public boolean existById(Object customerId) {
        return super.existsById((String) customerId);
    }

    @Override
    public Page<Customer> getAll(Pageable pageable, Sort sort) {
        if (pageable == null && sort == null) {
            List<Customer> results = this.findAll();
            return new PageImpl<>(results, Pageable.unpaged(), results.size());
        }
        if (pageable != null && sort == null) {
            return this.findAll(pageable);
        }
        if (pageable == null) {
            List<Customer> results = this.findAll(sort);
            return new PageImpl<>(results, Pageable.unpaged(), results.size());
        }
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return this.findAll(newPageable);
    }

    @Override
    public Page<Customer> getCustomersUsingJoin(Collection<Join> joins, Collection<Condition> conditions, Pageable pageable, Sort sort) {
        Specification<Customer> spec = Specification.where(((root, query, criteriaBuilder) -> {
            joins.forEach(join -> root.join(join.getAttribute(), join.getType()));
            return new ConditionExecutor(criteriaBuilder, conditions).execute(root);
        }));
        if (pageable == null && sort == null) {
            List<Customer> results = this.findAll(spec);
            return new PageImpl<>(results, Pageable.unpaged(), results.size());
        }
        if (pageable != null && sort == null) {
            return this.findAll(spec, pageable);
        }
        if (pageable == null) {
            List<Customer> results = this.findAll(spec, sort);
            return new PageImpl<>(results, Pageable.unpaged(), results.size());
        }
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return this.findAll(spec, newPageable);
    }

    @Override
    @Transactional(propagation = MANDATORY)
    @Async
    public void saveAllCustomer(Collection<Customer> customers) {
        customerCount.set(customerCount.get() + this.saveAll(customers).size());
    }

    @Override
    @Transactional(propagation = MANDATORY)
    @Async
    public void deleteAllCustomer(Collection<Customer> customers) {
        this.deleteAllInBatch(customers);
    }
}
