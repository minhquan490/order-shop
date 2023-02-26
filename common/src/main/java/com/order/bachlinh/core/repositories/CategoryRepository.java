package com.order.bachlinh.core.repositories;

import com.order.bachlinh.core.entities.model.Category;
import org.springframework.data.domain.Page;

public interface CategoryRepository {

    Category getCategoryByName(String categoryName);

    Category getCategoryById(String categoryId);

    Category saveCategory(Category category);

    Category updateCategory(Category category);

    boolean deleteCategory(Category category);

    Page<Category> getCategories();
}
