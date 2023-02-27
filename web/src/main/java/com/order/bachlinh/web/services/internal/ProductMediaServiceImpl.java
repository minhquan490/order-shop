package com.order.bachlinh.web.services.internal;

import com.order.bachlinh.core.entities.model.ProductMedia;
import com.order.bachlinh.core.entities.repositories.ProductMediaRepository;
import com.order.bachlinh.core.util.FileUtils;
import com.order.bachlinh.web.services.spi.common.ProductMediaService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ProductMediaServiceImpl implements ProductMediaService {
    private final ProductMediaRepository productMediaRepository;

    @Override
    public byte[] serve(String id) {
        ProductMedia media = productMediaRepository.loadMedia(Integer.parseInt(id));
        return FileUtils.readData(Path.of(media.getUrl()));
    }

    @Override
    public void write(HttpServletResponse response, byte[] data) throws IOException {
        response.getOutputStream().write(data);
    }
}
