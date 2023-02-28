package com.order.bachlinh.web.controller.rest.product;

import com.order.bachlinh.web.component.dto.resp.ProductResp;
import com.order.bachlinh.web.services.spi.common.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductInformationRestController {
    private static final String PRODUCT_INFORMATION = "/content/product/{id}";

    private final ProductService productService;

    @GetMapping(path = PRODUCT_INFORMATION)
    public ResponseEntity<ProductResp> productInformation(@PathVariable("id") String productId) {
        ProductResp result = productService.getProductById(productId);
        return ResponseEntity.ok(result);
    }
}
