package com.order.bachlinh.web.services.internal;

import com.order.bachlinh.core.entities.model.Cart;
import com.order.bachlinh.core.entities.model.Customer;
import com.order.bachlinh.core.entities.model.RefreshToken;
import com.order.bachlinh.core.entities.repositories.CartRepository;
import com.order.bachlinh.core.entities.repositories.CustomerRepository;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.security.handler.ClientSecretHandler;
import com.order.bachlinh.core.security.token.spi.TokenManager;
import com.order.bachlinh.web.component.dto.form.CreateCustomerForm;
import com.order.bachlinh.web.component.dto.form.CustomerUpdateForm;
import com.order.bachlinh.web.component.dto.form.LoginForm;
import com.order.bachlinh.web.component.dto.form.RegisterForm;
import com.order.bachlinh.web.component.dto.resp.CustomerResp;
import com.order.bachlinh.web.component.dto.resp.CustomerInformationResp;
import com.order.bachlinh.web.component.dto.resp.LoginResp;
import com.order.bachlinh.web.component.dto.resp.RegisterResp;
import com.order.bachlinh.web.services.spi.common.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final ClientSecretHandler clientSecretHandler;

    @Autowired
    CustomerServiceImpl(ApplicationContext applicationContext) {
        this.customerRepository = applicationContext.getBean(CustomerRepository.class);
        this.cartRepository = applicationContext.getBean(CartRepository.class);
        this.passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        this.tokenManager = applicationContext.getBean(TokenManager.class);
        this.entityFactory = applicationContext.getBean(EntityFactory.class);
        this.clientSecretHandler = applicationContext.getBean(ClientSecretHandler.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public LoginResp login(LoginForm loginForm) {
        if (loginForm.username() == null || loginForm.username().isBlank() || loginForm.password() == null || loginForm.password().isBlank()) {
            return new LoginResp(null, null, false);
        }
        Customer customer = customerRepository.getCustomerByUsername(loginForm.username());
        if (customer == null) {
            return new LoginResp(null, null, false);
        }
        if (!passwordEncoder.matches(loginForm.password(), customer.getPassword())) {
            return new LoginResp(null, null, false);
        }
        tokenManager.encode("customerId", customer.getId());
        tokenManager.encode("username", customer.getUsername());
        String accessToken = tokenManager.getTokenValue();
        RefreshToken refreshToken = tokenManager.getRefreshTokenGenerator().generateToken(customer.getId(), customer.getUsername());
        return new LoginResp(refreshToken, accessToken, true);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public RegisterResp register(RegisterForm registerForm) {
        Customer customer = registerForm.toCustomer(entityFactory, passwordEncoder);
        Cart cart = entityFactory.getEntity(Cart.class);
        cart.setCustomer(customer);
        customer.setCart(cart);
        if (!saveCustomer(customer)) {
            return new RegisterResp(REGISTER_FAILURE_MSG, true);
        }
        return new RegisterResp(REGISTER_SUCCESS_MSG, false);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean logout(Customer customer) {
        customer = customerRepository.getCustomerById(customer.getId());
        clientSecretHandler.removeClientSecret(customer.getRefreshToken().getRefreshTokenValue(), null);
        customer.setRefreshToken(null);
        return customerRepository.updateCustomer(customer) != null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteCustomer(String customerId) {
        Customer customer = customerRepository.getCustomerById(customerId);
        customer.setEnabled(false);
        customerRepository.updateCustomer(customer);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean createCustomer(CreateCustomerForm form) {
        Customer customer = CreateCustomerForm.toCustomer(form, entityFactory, passwordEncoder);
        return this.saveCustomer(customer);
    }

    @Override
    public boolean updateCustomer(CustomerUpdateForm form) {
        Customer customer = customerRepository.getCustomerById(form.getId());
        customer.setFirstName(form.getFirstName());
        customer.setLastName(form.getLastName());
        customer.setEmail(form.getEmail());
        customer.setPhoneNumber(form.getPhoneNumber());
        return customerRepository.updateCustomer(customer) != null;
    }

    @Override
    public CustomerInformationResp getCustomerInformation(String customerId) {
        Customer customer = customerRepository.getCustomerById(customerId);
        return CustomerInformationResp.toDto(customer);
    }

    @Override
    public Page<CustomerResp> getFullInformationOfCustomer(Pageable pageable) {
        return new PageImpl<>(
                customerRepository.getAll(pageable, null)
                .stream()
                .map(CustomerResp::toDto)
                .toList());
    }

    private boolean saveCustomer(Customer customer) {
        return customerRepository.saveCustomer(customer) != null && cartRepository.saveCart(customer.getCart()) != null;
    }
}
