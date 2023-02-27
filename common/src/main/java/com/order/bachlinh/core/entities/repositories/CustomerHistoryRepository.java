package com.order.bachlinh.core.entities.repositories;

import com.order.bachlinh.core.entities.model.Customer;
import com.order.bachlinh.core.entities.model.CustomerHistory;

import java.sql.Date;
import java.util.Collection;

public interface CustomerHistoryRepository {

    CustomerHistory saveCustomerHistory(CustomerHistory customerHistory);

    boolean deleteCustomerHistory(CustomerHistory customerHistory);

    boolean deleteAll(Collection<CustomerHistory> histories);

    Collection<CustomerHistory> getHistories(Customer customer);

    Collection<CustomerHistory> getHistoriesExpireNow(Date now);
}
