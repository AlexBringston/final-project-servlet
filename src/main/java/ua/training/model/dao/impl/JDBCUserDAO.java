package ua.training.model.dao.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.UserDAO;
import ua.training.model.dao.mappers.UserMapper;
import ua.training.model.entities.Role;
import ua.training.model.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCUserDAO implements UserDAO {

    private final Connection connection;
    private final Logger logger = LogManager.getLogger(JDBCUserDAO.class);

    public JDBCUserDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(User entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(name, surname, " +
                "username, password, " +
                "birth_date, role_id) VALUES (?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getSurname());
            preparedStatement.setString(3, entity.getUsername());
            preparedStatement.setString(4, entity.getPassword());
            preparedStatement.setDate(5, Date.valueOf(entity.getBirthDate()));
            preparedStatement.setLong(6, entity.getRole().getId());
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException exception) {
            logger.log(Level.ERROR, exception.getMessage());
            return false;
        }
    }

    @Override
    public User findById(Long id) {
        User user = new User();
        try (PreparedStatement ps = connection.prepareCall("SELECT u.id AS user_id, u.name AS user_name, u.surname, u.username, u.password, " +
                "u.birth_date, u.is_account_non_blocked, r.id AS role_id, r.name AS role_name FROM users AS u JOIN " +
                "roles as r ON u.role_id = r.id WHERE u.id = ?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            UserMapper mapper = new UserMapper();
            if (rs.next()) {
                user = mapper.extractFromResultSet(rs);
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR, exception.getMessage());
            throw new RuntimeException("Cannot find user by id");
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareCall("SELECT u.id AS user_id, u.name AS user_name, u.surname, u.username, u.password, " +
                "u.birth_date, u.is_account_non_blocked, r.id AS role_id, r.name AS role_name FROM users AS u JOIN " +
                "roles as r ON u.role_id = r.id")) {
            ResultSet rs = ps.executeQuery();
            UserMapper mapper = new UserMapper();
            while (rs.next()) {
                users.add(mapper.extractFromResultSet(rs));
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR, exception.getMessage());
            throw new RuntimeException("Cannot find list of all users");
        }
        return users;
    }

    @Override
    public boolean update(User entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET name = ?, surname = ?, username = ?, " +
                "password = ?, birth_date = ?, role_id = ?, is_account_non_blocked = ? WHERE id = ?")) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getSurname());
            preparedStatement.setString(3, entity.getUsername());
            preparedStatement.setString(4, entity.getPassword());
            preparedStatement.setDate(5, Date.valueOf(entity.getBirthDate()));
            preparedStatement.setLong(6, entity.getRole().getId());
            preparedStatement.setBoolean(7, entity.isAccountNonLocked());
            preparedStatement.setLong(8, entity.getId());
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException exception) {
            logger.log(Level.ERROR, exception.getMessage());
            throw new RuntimeException("Could not update user");
        }
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<User> user = Optional.empty();
        try (PreparedStatement preparedStatement = connection.prepareCall("SELECT u.id AS user_id, u.name AS user_name," +
                " u.surname, u.username, u.password, " +
                "u.birth_date, u.is_account_non_blocked, r.id AS role_id, r.name AS role_name FROM users AS u JOIN " +
                "roles as r ON u.role_id = r.id WHERE username = ?")) {
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            UserMapper mapper = new UserMapper();
            if (rs.next()) {
                user = Optional.of(mapper.extractFromResultSet(rs));
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR, exception.getMessage());
            throw new RuntimeException("Cannot find user with given username");
        }
        return user;
    }

    @Override
    public List<User> findAllByRole(Role role, Integer limit, Integer offset, String sortField, String sortDirection) {
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareCall("SELECT u.id AS user_id, u.name AS user_name, " +
                "u.surname, u.username, u.password, " +
                "u.birth_date, u.is_account_non_blocked, r.id AS role_id, r.name AS role_name FROM users AS u JOIN " +
                "roles as r ON u.role_id = r.id WHERE role_id = ? ORDER BY " + sortField + " " + sortDirection + " LIMIT ? OFFSET ?;")) {
            preparedStatement.setLong(1, role.getId());
            preparedStatement.setLong(2, limit);
            preparedStatement.setLong(3, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UserMapper userMapper = new UserMapper();
                users.add(userMapper.extractFromResultSet(resultSet));
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR, exception.getMessage());
            throw new RuntimeException("Cannot find list of users with given role");
        }
        return users;
    }

    @Override
    public Integer countAllByRole(String role) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM users JOIN roles" +
                " ON users.role_id = roles.id WHERE roles.name = ?")) {
            preparedStatement.setString(1, role);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException exception) {
            logger.log(Level.ERROR, exception.getMessage());
            throw new RuntimeException("Cannot count all users with given role");
        }
    }
}
