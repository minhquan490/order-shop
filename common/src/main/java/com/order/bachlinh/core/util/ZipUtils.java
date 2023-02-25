package com.order.bachlinh.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@NoArgsConstructor(access = AccessLevel.NONE)
@Log4j2
public final class ZipUtils {

    public static ByteBuffer compress(Object data) {
        if (!data.getClass().isAssignableFrom(Serializable.class)) {
            log.warn("Given object not implement Serializable");
            return null;
        }
        byte[] bytesData = serialize(data);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             GZIPOutputStream compressor = new GZIPOutputStream(outputStream)) {
            compressor.write(bytesData);
            return ByteBuffer.wrap(outputStream.toByteArray());
        } catch (IOException e) {
            log.error("Can not compress data", e);
            return null;
        }
    }

    public static <T> T decompress(ByteBuffer buffer) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer.array());
             GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
             ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(gzipInputStream.readAllBytes()))) {
            @SuppressWarnings("unchecked")
            T result = (T) objectInputStream.readObject();
            return result;
        } catch (IOException | ClassNotFoundException e) {
            log.error("Can not decompress buffer", e);
            return null;
        }
    }

    private static byte[] serialize(Object data) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(data);
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("Has problem when serialize data", e);
            return new byte[0];
        }
    }
}
