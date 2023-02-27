package com.order.bachlinh.web.services.internal;

import com.order.bachlinh.core.entities.model.Product;
import com.order.bachlinh.core.entities.model.ProductMedia;
import com.order.bachlinh.core.entities.model.Product_;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.entities.repositories.ProductRepository;
import com.order.bachlinh.core.util.FileUtils;
import com.order.bachlinh.web.component.dto.req.FlushFileReq;
import com.order.bachlinh.web.services.spi.business.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@PropertySource("classpath:common.properties")
class FileUploadServiceImpl implements FileUploadService {

    private final String tempFilePath;
    private final String resourceFilePath;
    private final ProductRepository productRepository;
    private final EntityFactory entityFactory;
    private final ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    FileUploadServiceImpl(@Value("${shop.app.temp.location}") String tempFilePath,
                          @Value("${shop.app.resource.location}") String resourceFilePath,
                          ProductRepository productRepository,
                          EntityFactory entityFactory,
                          ThreadPoolTaskExecutor taskExecutor) {
        this.tempFilePath = tempFilePath;
        this.resourceFilePath = resourceFilePath;
        this.productRepository = productRepository;
        this.entityFactory = entityFactory;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public boolean handleMultipartFile(MultipartFile file) throws IOException {
        createTempDirectory(file.getName());
        return writeChunk(file.getBytes(), file.getName());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean catAndFlushFile(FlushFileReq req) throws IOException {
        Collection<Path> files = FileUtils.readDirectory(tempFilePath, req.fileName());
        AtomicBoolean result = new AtomicBoolean(true);
        for (Path file : files) {
            byte[] data = FileUtils.readData(file);
            boolean rs = FileUtils.appendData(data, resourceFilePath, req.fileName(), req.extension());
            result.set(Boolean.logicalAnd(rs, result.get()));
        }
        boolean deleted = Files.deleteIfExists(Path.of(tempFilePath, req.fileName()));
        result.set(Boolean.logicalAnd(deleted, result.get()));
        if (result.get()) {
            taskExecutor.execute(() -> updateProduct(req));
        }
        return result.get();
    }

    private void createTempDirectory(String name) throws IOException {
        FileUtils.createDirectory(tempFilePath, name.split("-")[0]);
    }

    private boolean writeChunk(byte[] pieces, String directoryName) throws IOException {
        String[] path = directoryName.split("-");
        return FileUtils.writeToFile(pieces, String.join("/", tempFilePath, path[0]), path[1], ".bin");
    }

    private void updateProduct(FlushFileReq req) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put(Product_.ID, req.productId());
        Product product = productRepository.getProductByCondition(conditions);
        ProductMedia media = entityFactory.getEntity(ProductMedia.class);
        media.setUrl(Path.of(resourceFilePath, req.fileName().concat(req.extension())).toString());
        media.setProduct(product);
        Set<ProductMedia> medias = product.getMedias();
        medias.add(media);
        product.setMedias(medias);
        productRepository.updateProduct(product);
    }
}
