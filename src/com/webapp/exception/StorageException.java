package com.webapp.exception;

import java.io.IOException;
import java.sql.SQLException;

public class StorageException extends RuntimeException {
    private final String uuid;

    public StorageException(String message) {
        this(message, null, null);
    }

    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    public StorageException(String message, Exception e) {
        this(message, null, e);
    }

    public StorageException(String message, String uuid, Exception e) {
        super(message, e);
        this.uuid = uuid;
    }

    public StorageException(SQLException e) {
        this(e.getMessage(), e);
    }

    public String getUuid() {
        return uuid;
    }
}
