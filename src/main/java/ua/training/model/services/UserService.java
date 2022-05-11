package ua.training.model.services;


import ua.training.model.dao.AbonnementDAO;
import ua.training.model.dao.DAOFactory;
import ua.training.model.dao.RoleDAO;
import ua.training.model.dao.UserDAO;
import ua.training.model.dao.impl.ConnectionManager;
import ua.training.model.dto.Page;
import ua.training.model.entities.Role;
import ua.training.model.entities.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    /**
     * DAOFactory instance to generate different DAOs
     */
    private final DAOFactory daoFactory = DAOFactory.getInstance();

    /**
     * Method used to find a user by username
     * @param username - username of user
     * @param connection - connection instance
     * @return - user instance
     * @throws SQLException - if user is not found
     */
    public User findUserByUsername(String username, Connection connection) throws SQLException {
        UserDAO userDAO = daoFactory.createUserDAO(connection);
        User user = userDAO.findByUsername(username).orElseThrow(() -> new RuntimeException("User was not " +
                "found"));
        if (!connection.isClosed()) {
            ConnectionManager.close(connection);
        }
        return user;
    }

    /**
     * Method used to register a new user
     * @param user - user instance
     * @param role - role name
     * @param connection - connection instance
     * @return - boolean if user was registered successfully or not
     * @throws SQLException - if role was not found
     */
    public boolean registerNewUser(User user, String role, Connection connection) throws SQLException {
        RoleDAO roleDAO = daoFactory.createRoleDAO(connection);
        UserDAO userDAO = daoFactory.createUserDAO(connection);
        user.setRole(roleDAO.findByName(role).orElseThrow(() -> new IllegalArgumentException("There is no such role")));
        boolean b = userDAO.create(user);
        if (!connection.isClosed()) {
            ConnectionManager.close(connection);
        }
        return b;
    }

    /**
     * Method used to remove librarian role from user
     * @param userId - id of user
     * @param connection - connection instance
     * @return - boolean if user was updated or not
     */
    public boolean removeLibrarianRole(Long userId, Connection connection) {
        UserDAO userDAO = daoFactory.createUserDAO(connection);
        RoleDAO roleDAO = daoFactory.createRoleDAO(connection);
        User user = userDAO.findById(userId);
        user.setRole(roleDAO.findByName("ROLE_READER").orElseThrow(() -> new IllegalArgumentException("There is no " +
                "such role")));
        System.out.println(user);
        return userDAO.update(user);
    }

    /**
     * Method used to change user status
     * @param action - action to be applied
     * @param userId - id of user
     * @param connection - connection instance
     * @return - boolean if user was updated or not
     */
    public boolean changeUserStatus(String action, Long userId, Connection connection) {
        UserDAO userDAO = daoFactory.createUserDAO(connection);
        User user = userDAO.findById(userId);
        if (action.equals("block")) {
            user.setAccountNonLocked(false);
        }
        if (action.equals("unblock")) {
            user.setAccountNonLocked(true);
        }

        return userDAO.update(user);
    }

    /**
     * Method used to find ages of list of users
     * @param users - list of users
     * @return - list of users ages
     */
    public List<Integer> findUsersAges(List<User> users) {
        return users.stream()
                .map(user -> Period.between(user.getBirthDate(), LocalDate.now()).getYears())
                .collect(Collectors.toList());
    }

    /**
     * Method used to count books taken by each user in list
     * @param users - list of users
     * @param connection - connection instance
     * @return - list of numbers of counted books
     */
    public List<Integer> countBooksInUsersAbonnements(List<User> users, Connection connection) {
        AbonnementDAO abonnementDAO = daoFactory.createAbonnementDAO(connection);
        return users.stream()
                .map(user -> abonnementDAO.countAllBooksOfUser(user.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Method used to get users by role per page
     * @param role - role wanted
     * @param limit - limit of instances per page
     * @param page - current page number
     * @param sortField - field of sorting
     * @param sortDirection - direction of sorting
     * @param connection - connection instance
     * @return - page object of users on current page
     */
    public Page<User> getUsersByRolePerPage(String role, Integer limit, Integer page,
                                            String sortField, String sortDirection, Connection connection) {
        UserDAO userDAO = daoFactory.createUserDAO(connection);
        RoleDAO roleDAO = daoFactory.createRoleDAO(connection);
        Role roleByName = roleDAO.findByName(role).orElseThrow(() -> new IllegalArgumentException("There is no " +
                "such role"));
        Integer totalUsersWithRole = userDAO.countAllByRole(role);
        int pages = totalUsersWithRole / limit + (totalUsersWithRole % limit == 0 ? 0 : 1);
        Integer offset = page * limit;
        if (page > pages || page < 0) {
            throw new RuntimeException("Invalid page");
        }
        List<User> users = userDAO.findAllByRole(roleByName, limit, offset, sortField, sortDirection);

        return new Page.Builder<User>()
                .objects(users)
                .firstPage(0)
                .lastPage(pages - 1)
                .currentPage(page)
                .totalPages(pages - 1)
                .sortDirection(sortDirection)
                .sortField(sortField)
                .build();
    }

    /**
     * Method used to find user by id
     * @param userId - id of seeked user
     * @param connection - connection instance
     * @return - found user instance
     */
    public User findUserById(Long userId, Connection connection) {
        UserDAO userDAO = daoFactory.createUserDAO(connection);
        return userDAO.findById(userId);
    }

}
