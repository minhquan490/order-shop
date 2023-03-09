package com.order.bachlinh.core.component.client.template.spi;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

public interface RestTemplate {

    JsonNode get(String url, @Nullable Object body, @Nullable MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws IOException;
    JsonNode put(String url, Object body, @Nullable MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws IOException;
    JsonNode post(String url, Object body, @Nullable MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws IOException;
    JsonNode delete(String url, @Nullable Object body, @Nullable MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws IOException;
    ByteBuffer get(String url, @Nullable ByteBuffer body, @Nullable MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws IOException;
    ByteBuffer put(String url, ByteBuffer body, @Nullable MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws IOException;
    ByteBuffer post(String url, ByteBuffer body, @Nullable MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws IOException;
    ByteBuffer delete(String url, @Nullable ByteBuffer body, @Nullable MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws IOException;
}