package com.trainbooking.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    private static final String PROPERTIES_FILE = "config.properties";
    private static final Properties properties = loadProperties();

    private static Properties loadProperties() {
        Properties props = new Properties();
        // Load properties from the classpath first, fallback to file system
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE) != null
                ? DBConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)
                : new FileInputStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new IOException("Unable to find resource " + PROPERTIES_FILE);
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database configuration from " + PROPERTIES_FILE, e);
        }
        return props;
    }

    public static Connection getConnection() throws Exception {
        String driver = properties.getProperty("jdbc.driver");
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        if (driver == null || url == null || user == null) {
            throw new IllegalStateException("Missing DB connection configuration in config.properties");
        }

        Class.forName(driver);
        return DriverManager.getConnection(url, user, password);
    }
}
