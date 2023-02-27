package com.order.bachlinh.core.security.filter;

import com.order.bachlinh.core.entities.model.Customer;
import com.order.bachlinh.core.exception.UnAuthorizationException;
import com.order.bachlinh.core.entities.repositories.CustomerRepository;
import com.order.bachlinh.core.security.handler.TokenAuthenticationFailureHandler;
import com.order.bachlinh.core.security.token.spi.PrincipalHolder;
import com.order.bachlinh.core.security.token.spi.RefreshTokenHolder;
import com.order.bachlinh.core.security.token.spi.TokenManager;
import com.order.bachlinh.core.util.HeaderUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Filter use for authentication user with jwt and refresh token
 * before processing a request.
 *
 * @author Hoang Minh Quan.
 * */
public class AuthenticationFilter extends AbstractWebFilter {
    private final TokenManager tokenManager;
    private final CustomerRepository customerRepository;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final Collection<String> excludePaths;

    public AuthenticationFilter(ApplicationContext applicationContext, Collection<String> excludePaths) {
        super(applicationContext);
        this.tokenManager = applicationContext.getBean(TokenManager.class);
        this.customerRepository = applicationContext.getBean(CustomerRepository.class);
        this.authenticationFailureHandler = new TokenAuthenticationFailureHandler();
        this.excludePaths = excludePaths;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getServletPath();
        if (excludePaths.contains(requestPath) || requestPath.startsWith("/resource")) {
            filterChain.doFilter(request, response);
        }

        if ((response.getStatus() != HttpStatus.OK.value()) || response.isCommitted()) {
            return;
        }
        String jwtToken = HeaderUtils.getAuthorizeHeader(request);
        String refreshToken = HeaderUtils.getRefreshHeader(request);
        Map<String, Object> claims = new HashMap<>();
        if (jwtToken == null && refreshToken == null) {
            authenticationFailureHandler.onAuthenticationFailure(request, response, new UnAuthorizationException("Missing token, login is required"));
            return;
        }
        if (jwtToken != null && refreshToken == null) {
            claims.putAll(tokenManager.getClaimsFromToken(jwtToken));
        }
        if (jwtToken == null) {
            jwtToken = tryToRevoke(refreshToken);
            claims.putAll(tokenManager.getClaimsFromToken(jwtToken));
        }
        if (claims.isEmpty()) {
            jwtToken = tryToRevoke(refreshToken);
            claims.putAll(tokenManager.getClaimsFromToken(jwtToken));
        }
        Customer customer = customerRepository.getCustomerById((String) claims.get("customerId"));
        if (customer == null) {
            authenticationFailureHandler.onAuthenticationFailure(request, response, new UnAuthorizationException("Missing token, login is required"));
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(new PrincipalHolder(customer));
        getApplicationContext().publishEvent(new CustomerHistoryTrackingEvent(customer, requestPath));
        filterChain.doFilter(request, response);
    }

    private String tryToRevoke(String refreshToken) {
        RefreshTokenHolder holder = tokenManager.validateRefreshToken(refreshToken);
        String jwt;
        if (holder.isNonNull()) {
            Customer customer = holder.getValue().getCustomer();
            tokenManager.encode("customerId", customer.getId());
            tokenManager.encode("username", customer.getUsername());
            jwt = tokenManager.getTokenValue();
        } else {
            throw new UnAuthorizationException("Token is expired");
        }
        return jwt;
    }

    /**
     * Event published before pass the filter.
     *
     * @author Hoang Minh Quan
     * */
    public static class CustomerHistoryTrackingEvent extends ApplicationEvent {
        private final String url;

        public CustomerHistoryTrackingEvent(Object source, String url) {
            super(source);
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
