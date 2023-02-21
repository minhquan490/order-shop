package com.order.bachlinh.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.bachlinh.core.exception.JsonConvertException;

public final class JacksonUtils {

    private JacksonUtils() {
        throw new UnsupportedOperationException("Can not instance JacksonUtils");
    }

    private static final ObjectMapper SINGLETON = new ObjectMapper();

    public static byte[] writeObjectAsBytes(Object value) {
        try {
            return SINGLETON.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new JsonConvertException("Can not convert [" + value.getClass().getSimpleName() + "] to json object", e);
        }
    }
}
