package ua.training.model.dao.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.RoleDAO;
import ua.training.model.dao.mappers.RoleMapper;
import ua.training.model.entities.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCRoleDAO implements RoleDAO {

    private final Connection connection;
    private final Logger logger = LogManager.getLogger(JDBCRoleDAO.class);

    public JDBCRoleDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Role entity) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("INSERT INTO roles (name) VALUES (?);")) {
            preparedStatement.setString(1, entity.getName());
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not create role");
        }
    }

    @Override
    public Role findById(Long id) {
        Role role = new Role();
        try (PreparedStatement preparedStatement =
                     connection.prepareCall("SELECT r.id AS role_id, r.name AS role_name FROM roles AS" +
                             " r WHERE r.id = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            RoleMapper roleMapper = new RoleMapper();
            if (rs.next()) {
                role = roleMapper.extractFromResultSet(rs);
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could find role");
        }
        return role;
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT r.id AS role_id, r.name AS role_name FROM roles AS r");
            RoleMapper roleMapper = new RoleMapper();
            while (rs.next()) {
                roles.add(roleMapper.extractFromResultSet(rs));
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not find list of roles");
        }
        return roles;
    }

    @Override
    public boolean update(Role entity) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("UPDATE roles SET name = ? WHERE id = ?;")) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setLong(2, entity.getId());
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not update role");
        }
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Optional<Role> findByName(String name) {
        Optional<Role> optionalRole = Optional.empty();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT * FROM roles WHERE name = ?")) {
            preparedStatement.setString(1, name);
            RoleMapper roleMapper = new RoleMapper();
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                optionalRole = Optional.of(roleMapper.extractFromResultSet(resultSet));
            }
        } catch (SQLException exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not find role by name");
        }
        return optionalRole;
    }
}
