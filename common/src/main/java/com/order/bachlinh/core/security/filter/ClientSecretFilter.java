package com.order.bachlinh.core.security.filter;

import com.order.bachlinh.core.exception.UnAuthorizationException;
import com.order.bachlinh.core.security.handler.ClientSecretHandler;
import com.order.bachlinh.core.security.handler.TokenAuthenticationFailureHandler;
import com.order.bachlinh.core.util.HeaderUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.UUID;

/**
 * Filter for determine client secret token before allow user to
 * get resources.
 *
 * @author Hoang Minh Quan
 * */
public class ClientSecretFilter extends AbstractWebFilter {
    private static final String CLIENT_COOKIE_KEY = "client-secret";
    private final AuthenticationFailureHandler handler;
    private final ClientSecretHandler clientSecretHandler;
    private final String clientUrl;

    public ClientSecretFilter(ApplicationContext applicationContext, String clientUrl) {
        super(applicationContext);
        handler = new TokenAuthenticationFailureHandler();
        clientSecretHandler = applicationContext.getBean(ClientSecretHandler.class);
        this.clientUrl = clientUrl;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (response.getStatus() != HttpStatus.OK.value()) {
            return;
        }
        Cookie[] cookies = request.getCookies();
        Cookie clientSecret = null;
        if (cookies == null) {
            clientSecret = generateCookie(request);
            response.addCookie(clientSecret);
            filterChain.doFilter(request, response);
            return;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(CLIENT_COOKIE_KEY)) {
                clientSecret = cookie;
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (clientSecret == null && authentication != null) {
            clientSecretHandler.removeClientSecret(HeaderUtils.getRefreshHeader(request), null);
            handler.onAuthenticationFailure(request, response, new UnAuthorizationException("It looks like you changed the device"));
            return;
        }
        if (clientSecret == null) {
            clientSecret = generateCookie(request);
        }
        response.addCookie(clientSecret);
        filterChain.doFilter(request, response);
    }

    private Cookie generateCookie(HttpServletRequest request) {
        String secret = UUID.randomUUID().toString();
        clientSecretHandler.setClientSecret(secret, HeaderUtils.getRefreshHeader(request));
        Cookie clientSecret = new Cookie(CLIENT_COOKIE_KEY, secret);
        clientSecret.setHttpOnly(true);
        clientSecret.setSecure(true);
        clientSecret.setDomain(clientUrl);
        clientSecret.setMaxAge(Integer.MAX_VALUE);
        return clientSecret;
    }
}
