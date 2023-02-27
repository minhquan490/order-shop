package com.order.bachlinh.web.services.internal;

import com.order.bachlinh.core.entities.model.Category;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.web.component.dto.form.CategoryForm;
import com.order.bachlinh.web.component.dto.resp.CategoryDto;
import com.order.bachlinh.core.entities.repositories.CategoryRepository;
import com.order.bachlinh.web.services.spi.common.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EntityFactory entityFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryDto saveCategory(CategoryForm form) {
        Category category = entityFactory.getEntity(Category.class);
        category.setName(form.name());
        category = categoryRepository.saveCategory(category);
        return new CategoryDto(category.getId(), category.getName());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public CategoryDto updateCategory(CategoryForm form) {
        return saveCategory(form);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteCategory(CategoryForm form) {
        Category category = categoryRepository.getCategoryById(form.name());
        if (categoryRepository.deleteCategory(category)) {
            return "Delete success";
        } else {
            return "Delete failure";
        }
    }

    @Override
    public List<CategoryDto> getCategories() {
        Page<CategoryDto> categories = categoryRepository.getCategories().map(category -> new CategoryDto((String) category.getId(), category.getName()));
        return categories.stream().toList();
    }
}
