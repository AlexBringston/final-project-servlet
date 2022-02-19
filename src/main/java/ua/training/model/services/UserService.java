package ua.training.model.services;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.*;
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

    private final DAOFactory daoFactory = DAOFactory.getInstance();
    private final Logger logger = LogManager.getLogger(UserService.class);

    public User findUserByUsername(String username, Connection connection) throws SQLException {
        UserDAO userDAO = daoFactory.createUserDAO(connection);
        User user = userDAO.findByUsername(username).orElseThrow(() -> new RuntimeException("User was not " +
                "found"));
        if (!connection.isClosed()) {
            ConnectionManager.close(connection);
        }
        return user;
    }

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

    public boolean removeLibrarianRole(Long userId, Connection connection) {
        UserDAO userDAO = daoFactory.createUserDAO(connection);
        RoleDAO roleDAO = daoFactory.createRoleDAO(connection);
        User user = userDAO.findById(userId);
        user.setRole(roleDAO.findByName("ROLE_READER").orElseThrow(() -> new IllegalArgumentException("There is no " +
                "such role")));
        System.out.println(user);
        return userDAO.update(user);
    }

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

    public List<Integer> findUsersAges(List<User> users) {
        return users.stream()
                .map(user -> Period.between(user.getBirthDate(), LocalDate.now()).getYears())
                .collect(Collectors.toList());
    }

    public List<Integer> countBooksInUsersAbonnements(List<User> users, Connection connection) {
        AbonnementDAO abonnementDAO = daoFactory.createAbonnementDAO(connection);
        return users.stream()
                .map(user -> abonnementDAO.countAllBooksOfUser(user.getId()))
                .collect(Collectors.toList());
    }

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

}
