package ua.training.model.dao.mappers;

import ua.training.model.entities.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorMapper implements ObjectMapper<Author> {
    @Override
    public Author extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new Author(resultSet.getLong("author_id"),resultSet.getString("author_name"),
                resultSet.getString("author_surname"));
    }
}
