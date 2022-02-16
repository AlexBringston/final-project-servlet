package ua.training.model.dao.impl;

import ua.training.model.dao.*;

import java.sql.Connection;

public class JDBCDAOFactory extends DAOFactory {
    @Override
    public AbonnementDAO createAbonnementDAO(Connection connection) {
        return new JDBCAbonnementDAO(connection);
    }

    @Override
    public AuthorDAO createAuthorDAO(Connection connection) {
        return new JDBCAuthorDAO(connection);
    }

    @Override
    public BookDAO createBookDAO(Connection connection) {
        return new JDBCBookDAO(connection);
    }

    @Override
    public PublisherDAO createPublisherDAO(Connection connection) {
        return new JDBCPublisherDAO(connection);
    }

    @Override
    public ReadingRoomDAO createReadingRoomDAO(Connection connection) {
        return new JDBCReadingRoomDAO(connection);
    }

    @Override
    public RequestDAO createRequestDAO(Connection connection) {
        return new JDBCRequestDAO(connection);
    }

    @Override
    public RoleDAO createRoleDAO(Connection connection) {
        return new JDBCRoleDAO(connection);
    }

    @Override
    public StatusDAO createStatusDAO(Connection connection) {
        return new JDBCStatusDAO(connection);
    }

    @Override
    public UserDAO createUserDAO(Connection connection) {
        return new JDBCUserDAO(connection);
    }
}
