package com.order.bachlinh.web.controller.rest.product;

import com.order.bachlinh.web.component.dto.form.ProductForm;
import com.order.bachlinh.web.component.dto.resp.ProductResp;
import com.order.bachlinh.web.services.spi.common.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateProductRestController {
    private static final String UPDATE_PRODUCT = "/admin/product/update";

    private final ProductService productService;

    @PutMapping(path = UPDATE_PRODUCT)
    public ResponseEntity<ProductResp> updateProduct(@RequestBody ProductForm form) {
        ProductResp resp = productService.updateProduct(form);
        return ResponseEntity.ok(resp);
    }
}
