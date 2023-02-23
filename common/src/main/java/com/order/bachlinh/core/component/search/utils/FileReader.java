package com.order.bachlinh.core.component.search.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;

public final class FileReader {
    private final String filePath;

    public FileReader(String filePath) {
        this.filePath = filePath;
    }

    public Collection<String> read(long positionToRead, long size) throws IOException {
        FileChannel fileChannel = obtainChannel(filePath);
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, positionToRead, size);
        byte[] data = new byte[mappedByteBuffer.remaining()];
        mappedByteBuffer.get(data);
        return Arrays.asList(new String(data, StandardCharsets.UTF_8).split(","));
    }

    private FileChannel obtainChannel(String storeFilePath) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(storeFilePath, "r")) {
            return randomAccessFile.getChannel();
        }
    }
}
