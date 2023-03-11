package com.order.bachlinh.server.loader.internal;

import com.order.bachlinh.server.loader.spi.ServerPropertiesLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class DefaultServerPropertiesLoader implements ServerPropertiesLoader {

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
                    System.setProperty(keyPairValue[0], classLoader.getResource(keyPairValue[1].split("classpath:")[1]).getPath());
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
