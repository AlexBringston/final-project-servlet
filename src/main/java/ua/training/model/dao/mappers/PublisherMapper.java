package ua.training.model.dao.mappers;

import ua.training.model.entities.Publisher;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PublisherMapper implements ObjectMapper<Publisher> {
    @Override
    public Publisher extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new Publisher(resultSet.getLong("publisher_id"), resultSet.getString("publisher_name"));
    }
}
