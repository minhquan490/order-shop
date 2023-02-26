package com.order.bachlinh.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.NONE)
@Log4j2
public final class FileUtils {

    public static boolean createDirectory(String path, String name) throws IOException {
        Path p = Path.of(path, name);
        if (Files.exists(p)) {
            return false;
        }
        Files.createDirectory(p);
        return true;
    }

    public static boolean writeToFile(byte[] data, String path, String name, String extension) throws IOException {
        Path p = Path.of(path, name.concat(extension));
        if (!Files.exists(p)) {
            Files.createFile(p);
        }
        FileChannel fileChannel;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(new File(p.toUri()), "rw")) {
            fileChannel = randomAccessFile.getChannel();
        }
        return fileChannel.write(ByteBuffer.wrap(data)) != 0;
    }

    public static Collection<Path> readDirectory(String path, String directoryName) throws IOException {
        try (Stream<Path> pathStream = Files.walk(Path.of(path, directoryName))) {
            return pathStream.toList();
        }
    }

    public static boolean appendData(byte[] data, String path, String name, String extension) throws IOException {
        Path p = Path.of(path, name.concat(extension));
        if (!Files.exists(p)) {
            Files.createFile(p);
        }
        FileChannel fileChannel;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(new File(p.toUri()), "rw")) {
            fileChannel = randomAccessFile.getChannel();
        }
        fileChannel.position(fileChannel.size());
        return fileChannel.write(ByteBuffer.wrap(data)) != 0;
    }

    public static byte[] readData(Path path) {
        FileChannel fileChannel;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(new File(path.toUri()), "r")) {
            fileChannel = randomAccessFile.getChannel();
            ByteBuffer result = ByteBuffer.allocate((int) fileChannel.size());
            fileChannel.read(result);
            return result.array();
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
