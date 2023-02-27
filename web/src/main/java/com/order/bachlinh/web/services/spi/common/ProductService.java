package com.order.bachlinh.web.services.spi.common;

import com.order.bachlinh.web.component.dto.form.ProductForm;
import com.order.bachlinh.web.component.dto.form.ProductSearchForm;
import com.order.bachlinh.web.component.dto.resp.ProductResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface ProductService {

    ProductResp getProductByName(String productName);

    ProductResp getProductById(String productId);

    ProductResp saveProduct(ProductForm form);

    ProductResp updateProduct(ProductForm form);

    String deleteProduct(String productId);

    Page<ProductResp> productList(Pageable pageable);

    Page<ProductResp> searchProduct(ProductSearchForm form, Pageable pageable);

    Page<ProductResp> getProductsWithId(Collection<Object> ids);
}
