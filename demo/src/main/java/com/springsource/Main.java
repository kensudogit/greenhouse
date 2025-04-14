package com.springsource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    // Create a logger instance
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Use the logger to log the message
        logger.info("Hello world!");
    }
}