package com.order.bachlinh.web.services.spi.business;

import com.order.bachlinh.web.component.dto.req.FlushFileReq;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    boolean handleMultipartFile(MultipartFile file) throws IOException;

    boolean catAndFlushFile(FlushFileReq req) throws IOException;
}
