package com.order.bachlinh.web.controller.rest.product;

import com.order.bachlinh.web.component.dto.form.ProductSearchForm;
import com.order.bachlinh.web.component.dto.resp.ProductResp;
import com.order.bachlinh.web.services.spi.business.ProductSearchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductSearchRestController {
    private static final String SEARCH_PRODUCT = "/content/product/search";

    private final ProductSearchingService searchingService;

    @PostMapping(path = SEARCH_PRODUCT)
    public ResponseEntity<Page<ProductResp>> search(@RequestBody ProductSearchForm form) {
        PageRequest pageRequest = PageRequest.of(form.page(), form.size());
        Page<ProductResp> results;
        if (form.mode().equals("f")) {
            results = searchingService.fullTextSearch(form, pageRequest);
        } else {
            results = searchingService.search(form, pageRequest);
        }
        return ResponseEntity.ok(results);
    }
}
