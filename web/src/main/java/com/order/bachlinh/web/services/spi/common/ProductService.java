package com.order.bachlinh.web.services.spi.common;

import com.order.bachlinh.web.component.dto.form.ProductForm;
import com.order.bachlinh.web.component.dto.form.ProductSearchForm;
import com.order.bachlinh.web.component.dto.resp.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface ProductService {

    ProductDto getProductByName(String productName);

    ProductDto getProductById(String productId);

    ProductDto saveProduct(ProductForm form);

    ProductDto updateProduct(ProductForm form);

    String deleteProduct(String productId);

    Page<ProductDto> productList(Pageable pageable);

    Page<ProductDto> searchProduct(ProductSearchForm form, Pageable pageable);

    Page<ProductDto> getProductsWithId(Collection<Object> ids);
}
