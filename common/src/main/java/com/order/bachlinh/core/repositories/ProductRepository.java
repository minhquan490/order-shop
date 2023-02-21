package com.order.bachlinh.core.repositories;

import com.order.bachlinh.core.entities.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ProductRepository {

    Product saveProduct(Product product);

    Product updateProduct(Product product);

    boolean deleteProduct(Product product);

    boolean productNameExist(Product product);

    Product getProductByCondition(Map<String, Object> conditions);

    Page<Product> getProductsByCondition(Map<String, Object> conditions, Pageable pageable);
}
