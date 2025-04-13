package org.springframework.data;

public class S3FileStorageException extends RuntimeException {
    public S3FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}