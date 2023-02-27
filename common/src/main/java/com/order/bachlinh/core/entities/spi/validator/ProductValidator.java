package com.order.bachlinh.core.entities.spi.validator;

import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.model.Product;
import com.order.bachlinh.core.entities.spi.AbstractValidator;
import com.order.bachlinh.core.entities.spi.ValidateResult;
import com.order.bachlinh.core.entities.repositories.ProductRepository;
import org.springframework.context.ApplicationContext;

public class ProductValidator extends AbstractValidator {

    private final ProductRepository productRepository;

    public ProductValidator(ApplicationContext context) {
        super(context);
        this.productRepository = context.getBean(ProductRepository.class);
    }

    @Override
    public ValidateResult validate(BaseEntity entity) {
        Product product = (Product) entity;
        Result result = new Result();
        if (product.getName().length() > 100) {
            result.addMessageError("Product name: is greater than 100 character");
        }
        if (product.getName().isBlank()) {
            result.addMessageError("Product name: is blank");
        }
        if (productRepository.productNameExist(product)) {
            result.addMessageError("Product name: is exist");
        }
        if (product.getSize().length() > 3) {
            result.addMessageError("Product size: is greater than 3 character");
        }
        if (product.getSize().isBlank()) {
            result.addMessageError("Product size: is blank");
        }
        if (product.getColor().length() > 30) {
            result.addMessageError("Product color: is greater than 30 character");
        }
        if (product.getColor().isBlank()) {
            result.addMessageError("Product color: is blank");
        }
        if (product.getPrice() < 0) {
            result.addMessageError("Price: must be negative");
        }
        if (product.getCategories() == null || product.getCategories().isEmpty()) {
            result.addMessageError("Categories: product missing category");
        }
        return result;
    }
}
