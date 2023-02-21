package com.order.bachlinh.core.entities.spi.validator;

import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.model.Customer;
import com.order.bachlinh.core.entities.spi.AbstractValidator;
import com.order.bachlinh.core.entities.spi.ValidateResult;
import com.order.bachlinh.core.repositories.CustomerRepository;
import org.hibernate.validator.internal.util.DomainNameUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

public class CustomerValidator extends AbstractValidator {
    private final CustomerRepository customerRepository;

    public CustomerValidator(ApplicationContext context) {
        super(context);
        this.customerRepository = getApplicationContext().getBean(JpaRepositoryFactory.class).getRepository(CustomerRepository.class);
    }

    @Override
    public ValidateResult validate(BaseEntity entity) {
        Customer customer = (Customer) entity;
        Result result = new Result();
        if (customer.getUsername().length() > 24) {
            result.addMessageError("Username: is greater than 24 character");
        }
        if (customer.getUsername().isBlank()) {
            result.addMessageError("Username: is blank");
        }
        if (customerRepository.usernameExist(customer.getUsername())) {
            result.addMessageError("Username: is existed");
        }
        if (customer.getFirstName().length() > 36) {
            result.addMessageError("First name: is greater than 36 character");
        }
        if (customer.getFirstName().isBlank()) {
            result.addMessageError("First name: is blank");
        }
        if (customer.getLastName().length() > 36) {
            result.addMessageError("Last name: is greater than 36 character");
        }
        if (customer.getLastName().isBlank()) {
            result.addMessageError("Last name: is blank");
        }
        if (customer.getPhoneNumber().length() != 10) {
            result.addMessageError("Phone number: must have 10 number");
        }
        if (customerRepository.phoneNumberExist(customer.getPhoneNumber())) {
            result.addMessageError("Phone number: is existed");
        }
        if (customer.getEmail().length() > 32) {
            result.addMessageError("Email: is greater than 32 character");
        }
        if (customer.getEmail().isBlank()) {
            result.addMessageError("Email: is blank");
        }
        if (new InternalEmailValidator().isValid(customer.getEmail())) {
            result.addMessageError("Email: is not valid");
        }
        if (customerRepository.emailExist(customer.getEmail())) {
            result.addMessageError("Email: is exist");
        }
        if (customer.getGender().isEmpty()) {
            result.addMessageError("Gender: must be a male or female");
        }
        return result;
    }

    private static class InternalEmailValidator {
        private static final String LOCAL_PART_ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~\u0080-\uFFFF-]";
        private static final String LOCAL_PART_INSIDE_QUOTES_ATOM = "(?:[a-z0-9!#$%&'*.(),<>\\[\\]:;  @+/=?^_`{|}~\u0080-\uFFFF-]|\\\\\\\\|\\\\\\\")";
        private static final Pattern LOCAL_PART_PATTERN = Pattern.compile("(?:" + LOCAL_PART_ATOM + "+|\"" + LOCAL_PART_INSIDE_QUOTES_ATOM + "+\")" + "(?:\\." + "(?:" + LOCAL_PART_ATOM + "+|\"" + LOCAL_PART_INSIDE_QUOTES_ATOM + "+\")" + ")*", CASE_INSENSITIVE);

        public boolean isValid(String value) {
            int splitPosition = value.lastIndexOf('@');
            if (splitPosition < 0) {
                return false;
            }
            String localPart = value.substring(0, splitPosition);
            String domainPart = value.substring(splitPosition + 1);
            if (!isValidEmailLocalPart(localPart)) {
                return false;
            }
            return DomainNameUtil.isValidEmailDomainAddress(domainPart);
        }

        private boolean isValidEmailLocalPart(String localPart) {
            Matcher matcher = LOCAL_PART_PATTERN.matcher(localPart);
            return matcher.matches();
        }
    }
}
