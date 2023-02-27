package com.order.bachlinh.core.entities.repositories;

import com.order.bachlinh.core.entities.model.Order;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface OrderRepository extends JpaRepositoryImplementation<Order, String> {

    Order saveOrder(Order order);

    Order updateOrder(Order order);

    boolean deleteOrder(Order order);
}
