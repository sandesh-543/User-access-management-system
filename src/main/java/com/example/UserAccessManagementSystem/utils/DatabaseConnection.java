package com.example.UserAccessManagementSystem.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Utility class for managing database connections using HikariCP connection pooling.
 */
public class DatabaseConnection {
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource dataSource;

    static {
        // Load configuration from environment variables for security
        config.setJdbcUrl(System.getenv("DB_URL")); // Example: "jdbc:postgresql://localhost:5432/management"
        config.setUsername(System.getenv("DB_USER")); // Example: "sandesh"
        config.setPassword(System.getenv("DB_PASSWORD")); // Example: "12345678"

        // Optional configuration for performance tuning
        config.setMaximumPoolSize(10);         // Maximum number of connections in the pool
        config.setMinimumIdle(2);             // Minimum idle connections
        config.setIdleTimeout(30000);         // Close idle connections after 30 seconds
        config.setMaxLifetime(1800000);       // Maximum lifetime of a connection (30 minutes)
        config.setConnectionTimeout(10000);   // Timeout for getting a connection from the pool

        // Initialize the data source
        dataSource = new HikariDataSource(config);
    }

    /**
     * Provides a reusable database connection from the connection pool.
     *
     * @return a connected {@link Connection} object.
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Closes the connection pool when the application shuts down.
     * This is optional but useful for proper resource cleanup.
     */
    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}