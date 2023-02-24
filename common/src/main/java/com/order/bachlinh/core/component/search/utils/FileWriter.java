package com.order.bachlinh.core.component.search.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
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
        long channelSize = fileChannel.size();
        MappedByteBuffer backup = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, channelSize);
        boolean isNew = channelSize == 2;
        byte[] data = content.getBytes(StandardCharsets.UTF_8);
        ByteBuffer writableData = ByteBuffer.allocate((int) (channelSize + data.length));
        if (isNew) {
            content = content.concat(",");
        }
        do {
            if (backup.position() == positionToWrite) {
                for (byte piece : data) {
                    writableData.put(piece);
                }
            }
            writableData.put(backup.get());
        } while (backup.hasRemaining());
        writableData.flip();
        channelSizeCache += fileChannel.write(writableData);
        writableData.clear();
        fileChannel.close();
        return (int) (fileChannel.position() - content.length() - 1);
    }

    public long getFileSize() {
        return channelSizeCache;
    }

    private FileChannel obtainChannel(String storeFilePath) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(storeFilePath, "rw");
        return randomAccessFile.getChannel();
    }
}
