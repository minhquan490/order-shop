package com.order.bachlinh.web.repositories.internal;

import com.order.bachlinh.core.entities.model.Category;
import com.order.bachlinh.core.entities.model.Category_;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.repositories.AbstractRepository;
import com.order.bachlinh.web.repositories.spi.CategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.JoinType;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Primary
class CategoryRepositoryImpl extends AbstractRepository<Category, String> implements CategoryRepository {

    @Autowired
    CategoryRepositoryImpl(ApplicationContext applicationContext) {
        super(Category.class, applicationContext.getBean(EntityManager.class), applicationContext.getBean(SessionFactory.class), applicationContext.getBean(EntityFactory.class));
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        Specification<Category> spec = Specification.where((root, query, criteriaBuilder) -> {
            root.join(Category_.PRODUCTS, JoinType.INNER);
            return criteriaBuilder.equal(root.get(Category_.NAME), categoryName);
        });
        return this.get(spec);
    }

    @Override
    public Category getCategoryById(String categoryId) {
        Specification<Category> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Category_.ID), categoryId));
        return this.get(spec);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Category saveCategory(Category category) {
        return this.save(category);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.READ_COMMITTED)
    public Category updateCategory(Category category) {
        return saveCategory(category);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean deleteCategory(Category category) {
        if (category == null) {
            return false;
        }
        Specification<Category> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Category_.ID), category.getId()));
        return this.delete(spec) == 1;
    }

    @Override
    public Page<Category> getCategories() {
        return this.findAll(Pageable.unpaged());
    }
}
