package ua.training.model.dao;

import ua.training.model.dao.impl.JDBCDAOFactory;

import java.sql.Connection;

public abstract class DAOFactory {
    private static DAOFactory daoFactory;

    public abstract AbonnementDAO createAbonnementDAO(Connection connection);
    public abstract AuthorDAO createAuthorDAO(Connection connection);
    public abstract BookDAO createBookDAO(Connection connection);
    public abstract PublisherDAO createPublisherDAO(Connection connection);
    public abstract ReadingRoomDAO createReadingRoomDAO(Connection connection);
    public abstract RequestDAO createRequestDAO(Connection connection);
    public abstract RoleDAO createRoleDAO(Connection connection);
    public abstract StatusDAO createStatusDAO(Connection connection);
    public abstract UserDAO createUserDAO(Connection connection);

    public static DAOFactory getInstance() {
        if (daoFactory == null) {
            synchronized (DAOFactory.class) {
                if (daoFactory == null) {
                    daoFactory = new JDBCDAOFactory();
                }
            }
        }
        return daoFactory;
    }
}
