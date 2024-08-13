package com.basejava.webapp.sql;

import com.basejava.webapp.exception.ExistStorageException;
import com.basejava.webapp.exception.StorageException;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;

public class ExceptionUtil {
    private static final String UNIQUE_VIOLATION = "23505";

    private ExceptionUtil() {
    }

    public static StorageException convertException(SQLException e) {
        if (e instanceof PSQLException) {

//          http://www.postgresql.org/docs/9.3/static/errcodes-appendix.html
            if (e.getSQLState().equals(UNIQUE_VIOLATION)) {
                return new ExistStorageException(e);
            }
        }
        return new StorageException(e);
    }
}
