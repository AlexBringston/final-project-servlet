package ua.training.model.dao.mappers;

import ua.training.model.entities.Author;
import ua.training.model.entities.Book;
import ua.training.model.entities.Publisher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

public class BookMapper implements ObjectMapper<Book>{
    @Override
    public Book extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new Book.Builder()
                .id(resultSet.getLong("book_id"))
                .name(resultSet.getString("book_name"))
                .onlyForReadingHall(resultSet.getBoolean("only_for_reading_hall"))
                .available(resultSet.getBoolean("is_available"))
                .publisher(new Publisher(resultSet.getLong("publisher_id"),resultSet.getString("publisher_name")))
                .quantity(resultSet.getInt("quantity"))
                .publishedAt(resultSet.getDate("published_at").toLocalDate())
                .imgUrl(resultSet.getString("img_url"))
                .mainAuthor(new Author(resultSet.getLong("author_id"),resultSet.getString("author_name"),
                        resultSet.getString("author_surname")))
                .amountOfBooksTaken(resultSet.getInt("amount_of_books_taken"))
                .build();
    }
}
