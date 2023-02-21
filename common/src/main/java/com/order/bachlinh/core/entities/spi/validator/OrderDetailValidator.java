package com.order.bachlinh.core.entities.spi.validator;

import com.order.bachlinh.core.entities.model.OrderDetail;
import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.spi.AbstractValidator;
import com.order.bachlinh.core.entities.spi.ValidateResult;
import org.springframework.context.ApplicationContext;

public class OrderDetailValidator extends AbstractValidator {
    // inject

    public OrderDetailValidator(ApplicationContext context) {
        super(context);
    }

    @Override
    public ValidateResult validate(BaseEntity entity) {
        OrderDetail orderDetail = (OrderDetail) entity;
        Result result = new Result();
        if (orderDetail.getAmount() <= 0) {
            result.addMessageError("Order amount: must be a positive number");
        }
        return result;
    }
}
