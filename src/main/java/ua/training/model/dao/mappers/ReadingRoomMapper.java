package ua.training.model.dao.mappers;


import ua.training.model.entities.Book;
import ua.training.model.entities.ReadingRoom;
import ua.training.model.entities.Status;
import ua.training.model.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReadingRoomMapper implements ObjectMapper<ReadingRoom> {
    @Override
    public ReadingRoom extractFromResultSet(ResultSet resultSet) throws SQLException {

        ReadingRoom readingRoom = new ReadingRoom();
        readingRoom.setId(resultSet.getLong("room_id"));
        UserMapper userMapper = new UserMapper();
        User user = userMapper.extractFromResultSet(resultSet);
        readingRoom.setUser(user);

        Book book = new BookMapper().extractFromResultSet(resultSet);
        readingRoom.setBook(book);

        Status status = new StatusMapper().extractFromResultSet(resultSet);
        readingRoom.setStatus(status);
        return readingRoom;
    }
}
