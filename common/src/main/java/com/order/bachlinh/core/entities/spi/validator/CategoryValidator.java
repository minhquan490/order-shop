package com.order.bachlinh.core.entities.spi.validator;

import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.model.Category;
import com.order.bachlinh.core.entities.spi.AbstractValidator;
import com.order.bachlinh.core.entities.spi.ValidateResult;
import org.springframework.context.ApplicationContext;

public class CategoryValidator extends AbstractValidator {
    public CategoryValidator(ApplicationContext context) {
        super(context);
    }

    @Override
    public ValidateResult validate(BaseEntity entity) {
        Category category = ((Category) entity);
        Result result = new Result();
        if (category.getName().length() > 60) {
            result.addMessageError("Category name: is greater than 60");
        }
        if (category.getName().isBlank()) {
            result.addMessageError("Category name: is blank");
        }
        return result;
    }
}
