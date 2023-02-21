package com.order.bachlinh.core.entities.spi.validator;

import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.model.OrderStatus;
import com.order.bachlinh.core.entities.spi.AbstractValidator;
import com.order.bachlinh.core.entities.spi.ValidateResult;
import org.springframework.context.ApplicationContext;

public class OrderStatusValidator extends AbstractValidator {

    public OrderStatusValidator(ApplicationContext context) {
        super(context);
    }

    @Override
    public ValidateResult validate(BaseEntity entity) {
        OrderStatus orderStatus = (OrderStatus) entity;
        Result result = new Result();
        if (orderStatus.getStatus().isBlank()) {
            result.addMessageError("Order status: is blank");
        }
        return result;
    }
}
