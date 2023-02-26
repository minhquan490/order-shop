package com.order.bachlinh.core.repositories;

import com.order.bachlinh.core.entities.model.Address;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface AddressRepository extends JpaRepositoryImplementation<Address, String> {

    Address composeSave(Address address, CustomerRepository customerRepository);

    Address updateAddress(Address address);

    boolean deleteAddress(Address address);
}
