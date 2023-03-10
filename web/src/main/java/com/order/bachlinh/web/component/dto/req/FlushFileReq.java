package com.order.bachlinh.web.component.dto.req;

import com.fasterxml.jackson.annotation.JsonAlias;

public record FlushFileReq(@JsonAlias("file-name") String fileName,
                           @JsonAlias("extension") String extension,
                           @JsonAlias("product-id") String productId) {
}
