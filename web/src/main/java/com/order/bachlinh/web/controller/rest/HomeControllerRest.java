package com.order.bachlinh.web.controller.rest;

import com.order.bachlinh.web.component.dto.resp.ProductDto;
import com.order.bachlinh.web.services.spi.common.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class HomeControllerRest {
    private static final String HOME_URL = "/home";
    private final ProductService productService;

    @GetMapping(path = HOME_URL)
    @ResponseStatus(code = HttpStatus.OK)
    public Map<String, Object> home() {
        Page<ProductDto> productDtos = productService.productList(Pageable.ofSize(500));
        Set<String> categories = new HashSet<>();
        productDtos.stream()
                .map(ProductDto::categories)
                .distinct()
                .map(Arrays::asList)
                .collect(Collectors.toSet())
                .forEach(categories::addAll);
        Map<String, Object> resp = new HashMap<>();
        resp.put("products", productDtos);
        resp.put("categories", categories);
        return resp;
    }
}
