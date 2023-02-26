package com.order.bachlinh.core.component.search.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public record FileReader(String filePath) {

    public String read(long positionToRead, long size) throws IOException {
        FileChannel fileChannel = obtainChannel(filePath);
        MappedByteBuffer data = fileChannel.map(FileChannel.MapMode.READ_ONLY, positionToRead, size);
        ByteBuffer result = ByteBuffer.allocate((int) size);
        do {
            result.put(data.get());
        } while (data.position() < size);
        result.flip();
        fileChannel.close();
        return new String(result.array(), StandardCharsets.UTF_8);
    }

    private FileChannel obtainChannel(String storeFilePath) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(storeFilePath, "r");
        return randomAccessFile.getChannel();
    }
}
