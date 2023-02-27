package com.order.bachlinh.web.services.spi.common;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ProductMediaService {
    byte[] serve(String id);

    void write(HttpServletResponse response, byte[] data) throws IOException;
}
