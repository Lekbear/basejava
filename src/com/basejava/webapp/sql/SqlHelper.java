package com.basejava.webapp.sql;

import com.basejava.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @FunctionalInterface
    public interface CustomSqlInterface<T> {
        T accept(PreparedStatement ps) throws SQLException;
    }

    public <T> T execute(String sql, CustomSqlInterface<T> customSqlInterface) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            return customSqlInterface.accept(ps);
        } catch (SQLException e) {
            throw ExceptionUtil.convertException(e);
        }
    }

    @FunctionalInterface
    public interface CustomSqlInterfaceByConnection {
        void accept(PreparedStatement ps) throws SQLException;
    }

    public void executeByConnection(Connection connection,
                                    String sql,
                                    CustomSqlInterfaceByConnection customSqlInterfaceByConnection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            customSqlInterfaceByConnection.accept(ps);
        }
    }

    @FunctionalInterface
    public interface CustomSqlTransactionInterface {
        void accept(Connection connection) throws SQLException;
    }

    public void executeTransaction(CustomSqlTransactionInterface customSqlTransactionInterface) {
        try (Connection connection = connectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                customSqlTransactionInterface.accept(connection);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw ExceptionUtil.convertException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
