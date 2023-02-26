package com.order.bachlinh.web.services.spi.business;

import com.order.bachlinh.web.component.dto.form.ProductSearchForm;
import com.order.bachlinh.web.component.dto.resp.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductSearchingService {
    Page<ProductDto> search(ProductSearchForm form, Pageable pageable);

    Page<ProductDto> fullTextSearch(ProductSearchForm form, Pageable pageable);
}
