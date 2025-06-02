package com.webapp.sql;

import com.webapp.exception.ExistStorageException;
import com.webapp.exception.StorageException;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;

public class ExceptionUtil {
    private ExceptionUtil() {
    }

    public static StorageException convertException(SQLException e) {
        if (e instanceof PSQLException) {
            // Обработка специфичных ошибок PostgreSQL
            if (e.getSQLState().equals("23505")) { // Код состояния для дублирования ключа
                return new ExistStorageException(null);
            }
        }
        return new StorageException(e);
    }
}