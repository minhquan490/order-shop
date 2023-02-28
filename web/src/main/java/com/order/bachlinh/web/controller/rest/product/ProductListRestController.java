package com.order.bachlinh.web.controller.rest.product;

import com.order.bachlinh.web.component.dto.resp.ProductResp;
import com.order.bachlinh.web.services.spi.common.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProductListRestController {
    private static final String PRODUCT_LIST = "/content/product/list";

    private final ProductService productService;

    @GetMapping(path = PRODUCT_LIST)
    public ResponseEntity<Map<String, Object>> productList(@RequestParam(required = false, defaultValue = "1") String page,
                                                           @RequestParam(required = false, defaultValue = "100") String size) {
        PageRequest pageRequest = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
        Map<String, Object> resp = new HashMap<>();
        Page<ProductResp> results = productService.productList(pageRequest);
        resp.put("products", results);
        return ResponseEntity.ok(resp);
    }
}
