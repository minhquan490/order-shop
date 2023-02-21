package com.order.bachlinh.core.entities.spi.validator;

import com.order.bachlinh.core.entities.model.Address;
import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.spi.ValidateResult;
import com.order.bachlinh.core.entities.spi.AbstractValidator;
import org.springframework.context.ApplicationContext;

public class AddressValidator extends AbstractValidator {

    public AddressValidator(ApplicationContext context) {
        super(context);
    }

    @Override
    public ValidateResult validate(BaseEntity entity) {
        Result result = new Result();
        Address address = (Address) entity;
        if (address.getValue().isBlank()) {
            result.addMessageError("Detail address: is blank");
        }
        if (address.getCity().isBlank()) {
            result.addMessageError("City: is blank");
        }
        if (address.getCountry().isBlank()) {
            result.addMessageError("Country: is blank");
        }
        return result;
    }
}
