package com.order.bachlinh.web.controller.rest.product;

import com.order.bachlinh.web.services.spi.common.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DeleteProductRestController {
    private static final String DELETE_PRODUCT = "/admin/product/delete";

    private final ProductService productService;

    @DeleteMapping(path = DELETE_PRODUCT)
    public ResponseEntity<Object> deleteProduct(@RequestAttribute("id") String productId) {
        String result = productService.deleteProduct(productId);
        Map<String, String> resp = new HashMap<>();
        resp.put("message", result);
        return ResponseEntity.accepted().body(resp);
    }
}
