package com.order.bachlinh.web.controller.rest;

import com.order.bachlinh.web.services.spi.common.ProductMediaService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ServingFileRestController {
    private static final String RESOURCE_URL = "/resource/{id}";

    private final ProductMediaService productMediaService;

    @GetMapping(path = RESOURCE_URL)
    @ResponseStatus(HttpStatus.OK)
    public void serveResource(@PathVariable("id") String resourceId, HttpServletResponse response) throws IOException {
        byte[] data = productMediaService.serve(resourceId);
        productMediaService.write(response, data);
    }
}
