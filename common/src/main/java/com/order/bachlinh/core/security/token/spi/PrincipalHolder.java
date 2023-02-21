package com.order.bachlinh.core.security.token.spi;

import com.order.bachlinh.core.entities.model.Customer;
import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Subclass of {@link AbstractAuthenticationToken} for holding {@code Customer}
 * to used into a security system.
 *
 * @author Hoang Minh Quan.
 * */
@EqualsAndHashCode(callSuper = true)
public final class PrincipalHolder extends AbstractAuthenticationToken {

    private final Customer customer;

    public PrincipalHolder(Customer customer) {
        super(customer.getAuthorities());
        this.customer = customer;
    }

    @Override
    public Object getCredentials() {
        return customer;
    }

    @Override
    public Object getPrincipal() {
        return getCredentials();
    }
}
