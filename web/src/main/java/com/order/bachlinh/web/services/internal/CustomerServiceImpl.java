package com.order.bachlinh.web.services.internal;

import com.order.bachlinh.core.entities.model.Cart;
import com.order.bachlinh.core.entities.model.Customer;
import com.order.bachlinh.core.entities.model.RefreshToken;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.repositories.CustomerRepository;
import com.order.bachlinh.core.security.token.spi.TokenManager;
import com.order.bachlinh.web.component.dto.form.LoginForm;
import com.order.bachlinh.web.component.dto.form.RegisterForm;
import com.order.bachlinh.web.component.dto.resp.LoginDto;
import com.order.bachlinh.web.component.dto.resp.RegisterDto;
import com.order.bachlinh.core.repositories.CartRepository;
import com.order.bachlinh.web.services.spi.common.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
class CustomerServiceImpl implements CustomerService {
    private static final String REGISTER_FAILURE_MSG = "Register failure";
    private static final String REGISTER_SUCCESS_MSG = "Register success, please login to use service";
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenManager tokenManager;
    private final EntityFactory entityFactory;

    @Autowired
    CustomerServiceImpl(ApplicationContext applicationContext) {
        this.customerRepository = applicationContext.getBean(CustomerRepository.class);
        this.cartRepository = applicationContext.getBean(CartRepository.class);
        this.passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        this.tokenManager = applicationContext.getBean(TokenManager.class);
        this.entityFactory = applicationContext.getBean(EntityFactory.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public LoginDto login(LoginForm loginForm) {
        if (loginForm.username() == null || loginForm.username().isBlank() || loginForm.password() == null || loginForm.password().isBlank()) {
            return new LoginDto(null, null, false);
        }
        Customer customer = customerRepository.getCustomerByUsername(loginForm.username());
        if (customer == null) {
            return new LoginDto(null, null, false);
        }
        if (!passwordEncoder.matches(loginForm.password(), customer.getPassword())) {
            return new LoginDto(null, null, false);
        }
        tokenManager.encode("customerId", customer.getId());
        tokenManager.encode("username", customer.getUsername());
        String accessToken = tokenManager.getTokenValue();
        RefreshToken refreshToken = tokenManager.getRefreshTokenGenerator().generateToken(customer.getId(), customer.getUsername());
        return new LoginDto(refreshToken, accessToken, true);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public RegisterDto register(RegisterForm registerForm) {
        Customer customer = registerForm.toCustomer(entityFactory, passwordEncoder);
        Cart cart = entityFactory.getEntity(Cart.class);
        cart.setCustomer(customer);
        customer.setCart(cart);
        if (!saveCustomer(customer)) {
            return new RegisterDto(REGISTER_FAILURE_MSG, true);
        }
        return new RegisterDto(REGISTER_SUCCESS_MSG, false);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean logout(Customer customer) {
        customer = customerRepository.getCustomerById(customer.getId());
        customer.setRefreshToken(null);
        return customerRepository.updateCustomer(customer) != null;
    }

    private boolean saveCustomer(Customer customer) {
        return customerRepository.saveCustomer(customer) != null && cartRepository.saveCart(customer.getCart()) != null;
    }
}
