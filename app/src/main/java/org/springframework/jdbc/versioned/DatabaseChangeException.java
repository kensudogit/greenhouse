package org.springframework.jdbc.versioned;

public class DatabaseChangeException extends RuntimeException {
    public DatabaseChangeException(String message, Throwable cause) {
        super(message, cause);
    }
}