package com.basejava.webapp.sql;

import com.basejava.webapp.exception.ExistStorageException;
import com.basejava.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private static final String UNIQUE_VIOLATION = "23505";
    ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T> T execute(String sql, CustomSqlInterface<T> customSqlInterface) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            return customSqlInterface.accept(ps);
        } catch (SQLException e) {
            if (e.getSQLState().equals(UNIQUE_VIOLATION)) {
                throw new ExistStorageException(e);
            } else {
                throw new StorageException(e);
            }
        }
    }

    @FunctionalInterface
    public interface CustomSqlInterface<T> {
        T accept(PreparedStatement ps) throws SQLException;
    }
}
