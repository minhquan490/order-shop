package com.order.bachlinh.web.component.search.store.internal;

import com.order.bachlinh.web.component.search.index.internal.IndexManagerFactoryBuilderProvider;
import com.order.bachlinh.web.component.search.index.spi.IndexManager;
import com.order.bachlinh.web.component.search.store.spi.FileStoreType;
import com.order.bachlinh.web.component.search.store.spi.SingularStore;
import com.order.bachlinh.web.component.search.store.spi.StoreDescriptor;
import com.order.bachlinh.web.component.search.store.spi.StoreReader;
import com.order.bachlinh.web.component.search.store.spi.StoreWriter;
import com.order.bachlinh.web.component.search.utils.FileCreator;
import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.exception.CriticalException;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;

@Log4j2
class SingularStoreImplementer implements SingularStore {
    private static final String JSON_EXTENSION = ".json";
    private final Class<? extends BaseEntity> forEntity;
    private final StoreDescriptor storeDescriptor;
    private final StoreReader storeReader;
    private final StoreWriter storeWriter;
    private final IndexManager indexManager;

    SingularStoreImplementer(String name, String fileStorePath, Class<? extends BaseEntity> forEntity) {
        this.indexManager = IndexManagerFactoryBuilderProvider
                .getFactoryBuilder()
                .name(name)
                .path(fileStorePath)
                .build()
                .getIndexManager();
        this.storeDescriptor = new DefaultStoreDescriptor(true, FileStoreType.JSON, fileStorePath, name);
        this.storeWriter = buildStoreWriter(storeDescriptor, indexManager);
        this.storeReader = buildStoreReader(storeDescriptor, indexManager);
        this.forEntity = forEntity;
        try {
            createStoreFile(storeDescriptor);
            createIndexFile(storeDescriptor);
        } catch (IOException e) {
            throw new CriticalException("Can not create store " + name, e);
        }
    }

    @Override
    public Collection<String> getStoreValue(String keyWord) {
        return storeReader.read(keyWord);
    }

    @Override
    public String getName() {
        return storeDescriptor.getName();
    }

    @Override
    public StoreDescriptor getDescriptor() {
        return storeDescriptor;
    }

    @Override
    public boolean addValue(Map<String, String> value) {
        return storeWriter.write(value);
    }

    @Override
    public IndexManager getIndexManager() {
        return indexManager;
    }

    public Class<? extends BaseEntity> getForEntity() {
        return forEntity;
    }

    private StoreWriter buildStoreWriter(StoreDescriptor storeDescriptor, IndexManager indexManager) {
        return switch (storeDescriptor.getFileStoreType()) {
            case JSON -> new JsonStoreWriter(storeDescriptor, indexManager);
            case XML -> throw new UnsupportedOperationException("XML file is not supported");
            case DATABASE -> throw new UnsupportedOperationException("Database is not supported");
        };
    }

    private StoreReader buildStoreReader(StoreDescriptor storeDescriptor, IndexManager indexManager) {
        return switch (storeDescriptor.getFileStoreType()) {
            case JSON -> new JsonStoreReader(storeDescriptor, indexManager);
            case XML -> throw new UnsupportedOperationException("XML file is not supported");
            case DATABASE -> throw new UnsupportedOperationException("Database is not supported");
        };
    }

    private void createStoreFile(StoreDescriptor storeDescriptor) throws IOException {
        FileCreator creator = new FileCreator();
        String name = storeDescriptor.getName();
        if (name.endsWith(JSON_EXTENSION)) {
            String repairName = MessageFormat.format("{0}-{1}.json", name.split(JSON_EXTENSION)[0], "store");
            creator.createFile(storeDescriptor.getFileStorePath(), repairName, "", "{}");
        } else {
            creator.createFile(storeDescriptor.getFileStorePath(), name.concat("-store"), JSON_EXTENSION, "{}");
        }
    }

    private void createIndexFile(StoreDescriptor storeDescriptor) throws IOException {
        FileCreator creator = new FileCreator();
        String name = storeDescriptor.getName();
        if (name.endsWith(JSON_EXTENSION)) {
            String repairName = MessageFormat.format("{0}-{1}.json", name.split(JSON_EXTENSION)[0], "index");
            creator.createFile(storeDescriptor.getFileStorePath(), repairName, "", "{}");
        } else {
            creator.createFile(storeDescriptor.getFileStorePath(), name.concat("-index"), JSON_EXTENSION, "{}");
        }
    }
}

