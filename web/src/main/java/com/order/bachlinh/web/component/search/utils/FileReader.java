package com.order.bachlinh.web.component.search.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
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
        MappedByteBuffer data = fileChannel.map(FileChannel.MapMode.READ_ONLY, positionToRead, size);
        ByteBuffer result = ByteBuffer.allocate((int) size);
        do {
            result.put(data.get());
        } while (data.position() < size);
        result.flip();
        fileChannel.close();
        String resultString = new String(result.array(), StandardCharsets.UTF_8).split(":")[1];
        resultString = resultString
                .replace("[", "")
                .replace("]", "");
        return Arrays.asList(resultString.split(","));
    }

    private FileChannel obtainChannel(String storeFilePath) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(storeFilePath, "r");
        return randomAccessFile.getChannel();
    }
}
