package ua.training.model.dao.mappers;

import ua.training.model.entities.Book;
import ua.training.model.entities.Request;
import ua.training.model.entities.Status;
import ua.training.model.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestMapper implements ObjectMapper<Request> {

    @Override
    public Request extractFromResultSet(ResultSet resultSet) throws SQLException {
        Request request = new Request();
        request.setId(resultSet.getLong("id"));
        UserMapper userMapper = new UserMapper();
        User user = userMapper.extractFromResultSet(resultSet);
        request.setUser(user);

        Book book = new BookMapper().extractFromResultSet(resultSet);
        request.setBook(book);

        Status status = new StatusMapper().extractFromResultSet(resultSet);
        request.setStatus(status);
        return request;
    }
}
