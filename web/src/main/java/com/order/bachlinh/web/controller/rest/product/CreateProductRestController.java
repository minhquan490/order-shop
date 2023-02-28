package com.order.bachlinh.web.controller.rest.product;

import com.order.bachlinh.web.component.dto.form.ProductForm;
import com.order.bachlinh.web.component.dto.resp.ProductResp;
import com.order.bachlinh.web.services.spi.common.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateProductRestController {
    private static final String CREATE_PRODUCT = "/admin/product/create";

    private final ProductService productService;

    @PostMapping(path = CREATE_PRODUCT)
    public ResponseEntity<ProductResp> createProduct(@RequestBody ProductForm form) {
        ProductResp resp = productService.saveProduct(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
