package com.order.bachlinh.core.component.search.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public final class FileWriter {
    private final String filePath;
    private long channelSizeCache = 0;

    public FileWriter(String filePath) {
        this.filePath = filePath;
    }

    public int write(long positionToWrite, String content) throws IOException {
        FileChannel fileChannel = obtainChannel(filePath);
        if (positionToWrite < 0) {
            positionToWrite = (fileChannel.size() - 1);
        }
        int numberByteWrite = 0;
        try (fileChannel) {
            fileChannel.position(positionToWrite);
            ByteBuffer byteBuffer = ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));
            numberByteWrite = fileChannel.write(byteBuffer);
            return numberByteWrite;
        } finally {
            channelSizeCache += numberByteWrite;
            fileChannel.force(true);
        }
    }

    public long getFileSize() {
        return channelSizeCache;
    }

    private FileChannel obtainChannel(String storeFilePath) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(storeFilePath, "rw")) {
            return randomAccessFile.getChannel();
        }
    }
}
