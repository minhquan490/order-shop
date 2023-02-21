package com.order.bachlinh.core.component.client.template.spi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface RestTemplate {

    JsonNode get(String url, @Nullable Object body, @Nullable MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws JsonProcessingException;
    JsonNode put(String url, @Nullable Object body, @Nullable MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws JsonProcessingException;
    JsonNode post(String url, @Nullable Object body, @Nullable MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws JsonProcessingException;
    JsonNode delete(String url, @Nullable Object body, @Nullable MultiValueMap<String, String> headers, Map<String, ?> uriVariables) throws JsonProcessingException;
}
