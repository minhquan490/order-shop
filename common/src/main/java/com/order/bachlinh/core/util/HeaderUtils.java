package com.order.bachlinh.core.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

import java.util.Enumeration;
import java.util.HashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HeaderUtils {

    private static final String AUTHORIZE_HEADER = "Authorization";
    private static final String REFRESH_HEADER = "Refresh";

    public static String getRequestHeaderValue(String headerName, HttpServletRequest request) {
        return request.getHeader(headerName);
    }

    public static String getAuthorizeHeader(HttpServletRequest request) {
        return getRequestHeaderValue(AUTHORIZE_HEADER, request);
    }

    public static String getRefreshHeader(HttpServletRequest request) {
        return getRequestHeaderValue(REFRESH_HEADER, request);
    }

    public static MultiValueMap<String, Object> getRequestHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        MultiValueMap<String, Object> headers = new MultiValueMapAdapter<>(new HashMap<>());
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            request.getHeaders(headerName).asIterator().forEachRemaining(v -> headers.add(headerName, v));
        }
        return headers;
    }

    public static void setAuthorizeHeader(String token, HttpServletResponse response) {
        response.addHeader(AUTHORIZE_HEADER, token);
    }
}
