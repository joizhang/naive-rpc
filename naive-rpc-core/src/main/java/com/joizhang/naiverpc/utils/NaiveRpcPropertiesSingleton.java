package com.joizhang.naiverpc.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class NaiveRpcPropertiesSingleton {

    private static final String APPLICATION_PROPERTIES = "application.properties";

    private static volatile NaiveRpcPropertiesSingleton instance;

    private Properties properties = null;

    private NaiveRpcPropertiesSingleton() {
        // Read application.properties
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        String rpcConfigPath = "";
        if (url != null) {
            rpcConfigPath = url.getPath() + APPLICATION_PROPERTIES;
        }
        if (Files.exists(Paths.get(rpcConfigPath))) {
            try (FileInputStream fileInputStream = new FileInputStream(rpcConfigPath);
                 InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
                properties = new Properties();
                properties.load(inputStreamReader);
            } catch (IOException e) {
                log.error("Exception occurred when read properties file [{}]", APPLICATION_PROPERTIES);
            }
        } else {
            properties = new Properties();
        }
    }

    public static NaiveRpcPropertiesSingleton getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (NaiveRpcPropertiesSingleton.class) {
                if (Objects.isNull(instance)) {
                    instance = new NaiveRpcPropertiesSingleton();
                }
            }
        }
        return instance;
    }

    public Integer getIntegerValue(String key) {
        String value = getStringValue(key);
        return Integer.parseInt(value);
    }

    public String getStringValue(String key) {
        return properties.getProperty(key);
    }

    public String getStringValueOrDefault(String key, String defaultValue) {
        String value = getStringValue(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        return value;
    }

}
