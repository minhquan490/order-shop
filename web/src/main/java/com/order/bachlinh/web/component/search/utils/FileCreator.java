package com.order.bachlinh.web.component.search.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileCreator {

    public void createFile(String filePath, String fileName, String extension, String template) throws IOException {
        if (!extension.isEmpty() && !fileName.endsWith(extension)) {
            fileName = fileName.concat(extension);
        }
        Path path = Files.createFile(Path.of(filePath, fileName));
        File file = new File(path.toUri());
        if (!template.isEmpty()) {
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
                FileChannel channel = randomAccessFile.getChannel();
                channel.write(ByteBuffer.wrap(template.getBytes(StandardCharsets.UTF_8)));
                channel.close();
            }
        }
    }

    public void createFile(String filePath, String fileName, String extension) throws IOException {
        createFile(filePath, fileName, extension, "");
    }
}
