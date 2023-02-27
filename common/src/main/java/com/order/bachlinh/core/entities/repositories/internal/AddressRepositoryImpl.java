package com.order.bachlinh.core.entities.repositories.internal;

import com.order.bachlinh.core.entities.model.Address;
import com.order.bachlinh.core.entities.model.Address_;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.entities.repositories.CustomerRepository;
import com.order.bachlinh.core.entities.repositories.AbstractRepository;
import com.order.bachlinh.core.entities.repositories.AddressRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Primary
class AddressRepositoryImpl extends AbstractRepository<Address, String> implements AddressRepository {

    @Autowired
    AddressRepositoryImpl(@NonNull ApplicationContext applicationContext) {
        super(Address.class, applicationContext.getBean(EntityManager.class), applicationContext.getBean(SessionFactory.class), applicationContext.getBean(EntityFactory.class));
    }

    @Override
    @Transactional(propagation = MANDATORY)
    public Address composeSave(@NonNull Address address, @NonNull CustomerRepository customerRepository) {
        String customerId = address.getCustomer().getId();
        if (customerRepository.existById(customerId)) {
            customerRepository.saveCustomer(address.getCustomer());
        }
        return Optional.of(this.save(address)).orElse(null);
    }

    @Override
    @Transactional(propagation = MANDATORY, isolation = READ_COMMITTED)
    public Address updateAddress(Address address) {
        return this.save(address);
    }

    @Override
    @Transactional(propagation = MANDATORY)
    public boolean deleteAddress(Address address) {
        long numRowDeleted = this.delete(Specification.where(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Address_.ID), address.getId()))));
        return numRowDeleted == 0;
    }
}
