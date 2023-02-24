package com.order.bachlinh.web.services.spi.common;

import com.order.bachlinh.web.component.dto.form.CategoryForm;
import com.order.bachlinh.web.component.dto.resp.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto saveCategory(CategoryForm form);

    CategoryDto updateCategory(CategoryForm form);

    String deleteCategory(CategoryForm form);

    List<CategoryDto> getCategories();
}
