package com.order.bachlinh.core.security.filter;

import com.order.bachlinh.core.util.HeaderUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * Logging filter to log coming request.
 *
 * @author Hoang Minh Quan
 * */
@Log4j2
public class LoggingRequestFilter extends AbstractWebFilter {
    private final String clientUrl;

    public LoggingRequestFilter(ApplicationContext applicationContext, String clientUrl) {
        super(applicationContext);
        this.clientUrl = clientUrl;
    }


    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // TODO implement ddos protection
        logRequest(request);
        filterChain.doFilter(request, response);
    }

    /**
     * Log request metadata into rolling file.
     * */
    private void logRequest(HttpServletRequest request) {
        String userAgent = HeaderUtils.getRequestHeaderValue("User-Agent", request);
        String referer = HeaderUtils.getRequestHeaderValue("Referer", request);
        log.info("user-agent: {}", userAgent);
        log.info("user-locale: {}", request.getLocale());
        log.info("user-address: {}", request.getRemoteAddr());
        log.info("referer: {}", referer);
        log.info("request-path: {}", request.getServletPath());
        if (referer != null && !referer.contains(clientUrl)) {
            log.info("Request not come to client");
        }
    }
}
