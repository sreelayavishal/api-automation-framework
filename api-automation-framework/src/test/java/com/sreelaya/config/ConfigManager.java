package com.sreelaya.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton config loader.
 * Reads src/test/resources/config.properties once and caches it.
 */
public class ConfigManager {

    private static final Logger log = LogManager.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private final Properties props = new Properties();

    private ConfigManager() {
        try (InputStream in = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in == null) throw new RuntimeException("config.properties not found on classpath");
            props.load(in);
            log.info("config.properties loaded successfully");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) instance = new ConfigManager();
        return instance;
    }

    public String get(String key) {
        String value = props.getProperty(key);
        if (value == null) throw new RuntimeException("Missing config key: " + key);
        return value.trim();
    }

    public String getBaseUrl()       { return get("base.url"); }
    public String getAdminUsername() { return get("admin.username"); }
    public String getAdminPassword() { return get("admin.password"); }
}
