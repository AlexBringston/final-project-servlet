package ua.training.model.dao.mappers;

import ua.training.model.entities.Role;
import ua.training.model.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserMapper implements ObjectMapper<User>{

    @Override
    public User extractFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("user_id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setName(resultSet.getString("user_name"));
        user.setSurname(resultSet.getString("surname"));
        user.setBirthDate(LocalDate.parse(resultSet.getString("birth_date")));
        user.setAccountNonLocked(resultSet.getBoolean("is_account_non_blocked"));

        Role role = new Role(resultSet.getLong("role_id"), resultSet.getString("role_name"));
        user.setRole(role);
        return user;
    }
}
