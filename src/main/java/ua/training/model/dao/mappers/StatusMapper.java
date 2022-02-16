package ua.training.model.dao.mappers;

import ua.training.model.entities.Status;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusMapper implements ObjectMapper<Status> {
    @Override
    public Status extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new Status(resultSet.getLong("status_id"), resultSet.getString("status_name"));
    }
}
