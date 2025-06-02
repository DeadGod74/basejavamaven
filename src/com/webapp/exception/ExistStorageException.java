package com.webapp.exception;

import org.postgresql.util.PSQLException;

public class ExistStorageException extends StorageException {
    public ExistStorageException(String uuid) {
        super("Resume" + uuid + " already exists", uuid);
    }

    public ExistStorageException(String duplicateKeyError, PSQLException e) {
        super(duplicateKeyError, e);
    }
}
