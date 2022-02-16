package ua.training.model.dao.mappers;

import ua.training.model.entities.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleMapper implements ObjectMapper<Role> {
    @Override
    public Role extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new Role(resultSet.getLong("id"), resultSet.getString("name"));
    }
}
