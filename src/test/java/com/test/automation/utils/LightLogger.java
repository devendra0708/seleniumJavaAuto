package com.test.automation.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple lightweight logger for the framework
 * Provides basic logging functionality with minimal overhead
 */
public class LightLogger {
    
    private static final boolean DEBUG_ENABLED = Boolean.parseBoolean(System.getProperty("debug.logging", "false"));
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    
    /**
     * Log an info message
     */
    public static void info(String message) {
        log("INFO", message);
    }
    
    /**
     * Log a warning message
     */
    public static void warn(String message) {
        log("WARN", message);
    }
    
    /**
     * Log an error message
     */
    public static void error(String message) {
        log("ERROR", message);
    }
    
    /**
     * Log an error message with exception
     */
    public static void error(String message, Throwable e) {
        log("ERROR", message + " - " + e.getMessage());
    }
    
    /**
     * Log a debug message (only if debug logging is enabled)
     */
    public static void debug(String message) {
        if (DEBUG_ENABLED) {
            log("DEBUG", message);
        }
    }
    
    /**
     * Log a message with the specified level
     */
    private static void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println(timestamp + " [" + level + "] " + message);
    }
} 