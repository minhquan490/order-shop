package com.order.bachlinh.core.entities.repositories;

import com.order.bachlinh.core.entities.model.Cart;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface CartRepository extends JpaRepositoryImplementation<Cart, String> {

    Cart saveCart(Cart cart);

    Cart updateCart(Cart cart);

    boolean deleteCart(Cart cart);
}
