package com.order.bachlinh.server.loader.internal;

import com.order.bachlinh.server.loader.spi.ServerPropertiesLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class DefaultServerPropertiesLoader implements ServerPropertiesLoader {
    private static final String DELIMITER = "/";
    private final String resourcePath;

    DefaultServerPropertiesLoader() {
        String root = new File("").getAbsolutePath();
        this.resourcePath = String.join(DELIMITER, root, "server", "src", "main", "resources");
    }

    @Override
    public void loadDefault() {
        String defaultName = "application.properties";
        loadProperties(defaultName);
    }

    @Override
    public void load(String filePropertiesName) {
        loadProperties(filePropertiesName);
    }

    private void loadProperties(String fileName) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try {
            FileReader reader = new FileReader(classLoader.getResource(fileName).getPath());
            BufferedReader bufferedReader = new BufferedReader(reader, 2048);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] keyPairValue = line.split("=");
                if (keyPairValue[1].startsWith("classpath:")) {
                    System.setProperty(keyPairValue[0], String.join(DELIMITER, resourcePath, keyPairValue[1].split("classpath:")[1]));
                } else {
                    System.setProperty(keyPairValue[0], keyPairValue[1]);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new IllegalStateException("Has problem when config server", e);
        }
    }
}
