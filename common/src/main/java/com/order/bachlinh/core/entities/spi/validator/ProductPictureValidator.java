package com.order.bachlinh.core.entities.spi.validator;

import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.model.ProductMedia;
import com.order.bachlinh.core.entities.spi.AbstractValidator;
import com.order.bachlinh.core.entities.spi.ValidateResult;
import org.springframework.context.ApplicationContext;

public class ProductPictureValidator extends AbstractValidator {
    public ProductPictureValidator(ApplicationContext context) {
        super(context);
    }

    @Override
    public ValidateResult validate(BaseEntity entity) {
        ProductMedia productMedia = (ProductMedia) entity;
        Result result = new Result();
        if (productMedia.getUrl().isBlank()) {
            result.addMessageError("Url: url of picture must not be blank");
        }
        return result;
    }
}
