package com.order.bachlinh.web.repositories.spi;

import com.order.bachlinh.core.entities.model.Address;
import com.order.bachlinh.core.repositories.CustomerRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface AddressRepository extends JpaRepositoryImplementation<Address, String> {

    Address composeSave(Address address, CustomerRepository customerRepository);

    Address updateAddress(Address address);

    boolean deleteAddress(Address address);
}
