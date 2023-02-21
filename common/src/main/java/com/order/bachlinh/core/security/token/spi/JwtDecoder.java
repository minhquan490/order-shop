package com.order.bachlinh.core.security.token.spi;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;

/**
 * Decoder used for decode jwt token.
 *
 * @author Hoang Minh Quan
 * */
public interface JwtDecoder {

    /**
     * Decode jwt token string for access to payload of its.
     *
     * @param token given jwt token.
     * @return payload of jwt token.
     * @throws JwtException when process token failure.
     * */
    Jwt decode(String token) throws JwtException;
}
