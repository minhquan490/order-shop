package com.order.bachlinh.core.entities.spi.validator;

import com.order.bachlinh.core.entities.model.Order;
import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.spi.AbstractValidator;
import com.order.bachlinh.core.entities.spi.ValidateResult;
import org.springframework.context.ApplicationContext;

public class OrderValidator extends AbstractValidator {

    public OrderValidator(ApplicationContext context) {
        super(context);
    }

    @Override
    public ValidateResult validate(BaseEntity entity) {
        Order order = (Order) entity;
        Result result = new Result();
        if (order.getTotalDeposit() < 0) {
            result.addMessageError("Total deposit: must greater or equal 0");
        }
        if (order.getTimeOrder() == null) {
            result.addMessageError("Time order: must be specify");
        }
        return result;
    }
}
