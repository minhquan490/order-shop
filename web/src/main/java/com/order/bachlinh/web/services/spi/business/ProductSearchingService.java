package com.order.bachlinh.web.services.spi.business;

import com.order.bachlinh.web.component.dto.form.ProductSearchForm;
import com.order.bachlinh.web.component.dto.resp.ProductResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductSearchingService {
    Page<ProductResp> search(ProductSearchForm form, Pageable pageable);

    Page<ProductResp> fullTextSearch(ProductSearchForm form, Pageable pageable);
}
