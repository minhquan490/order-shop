package com.order.bachlinh.web.services.spi.common;

import com.order.bachlinh.web.component.dto.form.CategoryForm;
import com.order.bachlinh.web.component.dto.resp.CategoryResp;

import java.util.List;

public interface CategoryService {

    CategoryResp saveCategory(CategoryForm form);

    CategoryResp updateCategory(CategoryForm form);

    String deleteCategory(CategoryForm form);

    List<CategoryResp> getCategories();
}
