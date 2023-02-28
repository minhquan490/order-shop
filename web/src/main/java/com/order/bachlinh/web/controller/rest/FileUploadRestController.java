package com.order.bachlinh.web.controller.rest;

import com.order.bachlinh.web.component.dto.req.FlushFileReq;
import com.order.bachlinh.web.services.spi.business.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/file")
public class FileUploadRestController {
    private static final String UPLOAD_URL = "/upload";
    private static final String FLUSH_FILE_URL = "/flush";

    private final FileUploadService fileUploadService;

    @PostMapping(path = UPLOAD_URL)
    public ResponseEntity<Object> handleUpload(@RequestParam("file") MultipartFile file) throws IOException {
        boolean result = fileUploadService.handleMultipartFile(file);
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Flush all chunk files in the temp directory to the data directory. Must be called after upload.
     * */
    @PostMapping(path = FLUSH_FILE_URL)
    public ResponseEntity<Object> flushFile(@RequestBody FlushFileReq req) throws IOException {
        boolean result = fileUploadService.catAndFlushFile(req);
        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }
}
