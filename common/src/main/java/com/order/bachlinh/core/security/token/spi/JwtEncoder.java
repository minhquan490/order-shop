package com.order.bachlinh.core.security.token.spi;

import java.util.Map;

/**
 * Encoder for encoded attribute into payload of jwt token.
 *
 * @author Hoang Minh Quan.
 * */
public interface JwtEncoder {

    /**
     * Add attribute with key value into jwt token.
     * */
    void encode(String key, Object value);

    /**
     * Add payload into jwt token.
     * */
    void encode(Map<String, Object> payload);

    /**
     * Encode attribute and return value of it.
     *
     * @return value of jwt as string.
     * */
    String getTokenValue();
}
