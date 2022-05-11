package ua.training.model.dao.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.BookDAO;
import ua.training.model.dao.mappers.AuthorMapper;
import ua.training.model.dao.mappers.BookMapper;
import ua.training.model.entities.Author;
import ua.training.model.entities.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JDBCBookDAO implements BookDAO {

    private final Connection connection;
    private final Logger logger = LogManager.getLogger(JDBCBookDAO.class);

    public JDBCBookDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Book entity) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("INSERT INTO books (name, only_for_reading_hall, " +
                             "is_available, publisher_id, quantity, published_at, img_url, main_author_id," +
                             "amount_of_books_taken) VALUES(?,?,?,?,?,?,?,?,?)")) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setBoolean(2, entity.isOnlyForReadingHall());
            preparedStatement.setBoolean(3, entity.isAvailable());
            preparedStatement.setLong(4, entity.getPublisher().getId());
            preparedStatement.setInt(5, entity.getQuantity());
            preparedStatement.setDate(6, Date.valueOf(entity.getPublishedAt()));
            preparedStatement.setString(7, entity.getImgUrl());
            preparedStatement.setLong(8, entity.getMainAuthor().getId());
            preparedStatement.setInt(9, entity.getAmountOfBooksTaken());
            int result = preparedStatement.executeUpdate();
            return result != 0;
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not create a book");
        }
    }

    @Override
    public Book findById(Long id) {
        Book book = new Book.Builder().build();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT b.id AS book_id, b.name AS book_name, b.only_for_reading_hall, " +
                             "b.is_available, p.id AS " +
                             "publisher_id, p.name AS publisher_name, b.quantity, b.published_at, b.img_url, a.id as " +
                             "author_id, a.name as author_name, a.surname as author_surname, b.amount_of_books_taken " +
                             "FROM books AS b JOIN publishers as p ON p.id = b.publisher_id JOIN authors AS a on a.id" +
                             " = b.main_author_id WHERE b.id = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                BookMapper bookMapper = new BookMapper();
                book = bookMapper.extractFromResultSet(resultSet);
            }
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not find a book by given id");
        }
        return book;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT b.id as book_id, b.name as book_name, b" +
                    ".only_for_reading_hall, b.is_available, p.id AS publisher_id, p.name AS publisher_name, " +
                    "b.quantity, b.published_at, b.img_url, a.id as author_id, a.name as author_name, " +
                    "a.surname as author_surname, b.amount_of_books_taken " +
                    "FROM books AS b JOIN publishers as p ON p.id = b.publisher_id JOIN authors AS a on a.id" +
                    " = b.main_author_id");
            while (resultSet.next()) {
                BookMapper bookMapper = new BookMapper();
                books.add(bookMapper.extractFromResultSet(resultSet));
            }
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not find all books");
        }
        return books;
    }

    @Override
    public boolean update(Book entity) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("UPDATE books SET name = ?, only_for_reading_hall = ?, " +
                             "is_available = ?, publisher_id = ?, quantity = ?, published_at = ?, " +
                             "img_url = ?, main_author_id = ?, amount_of_books_taken = ? WHERE " +
                             "id = ?")) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setBoolean(2, entity.isOnlyForReadingHall());
            preparedStatement.setBoolean(3, entity.isAvailable());
            preparedStatement.setLong(4, entity.getPublisher().getId());
            preparedStatement.setInt(5, entity.getQuantity());
            preparedStatement.setDate(6, Date.valueOf(entity.getPublishedAt()));
            preparedStatement.setString(7, entity.getImgUrl());
            preparedStatement.setLong(8, entity.getMainAuthor().getId());
            preparedStatement.setInt(9, entity.getAmountOfBooksTaken());
            preparedStatement.setLong(10, entity.getId());
            System.out.println(preparedStatement);
            int result = preparedStatement.executeUpdate();
            return result != 0;
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not update a book");
        }
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Book> findBooksByGivenQuery(boolean isAvailable, String query, Integer limit, Integer offset, String sortField,
                                            String sortDirection) {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT b.id as book_id, b.name as book_name, b.only_for_reading_hall, b.is_available, p.id AS " +
                             "publisher_id, p.name AS publisher_name, b.quantity, b.published_at, b.img_url, a.id as " +
                             "author_id, a.name as author_name, a.surname as author_surname, b.amount_of_books_taken " +
                             "FROM books AS b JOIN publishers as p ON p.id = b.publisher_id JOIN authors AS a on a.id" +
                             " = b.main_author_id WHERE b.is_available = ? AND " +
                             "(UPPER(CONCAT(a.name, ' ', a.surname)) LIKE UPPER(CONCAT('%',?,'%'))" +
                             "OR UPPER(CONCAT(a.surname,' ', a.name)) LIKE UPPER(CONCAT('%',?,'%')) " +
                             "OR UPPER(b.name) LIKE UPPER(CONCAT('%',?,'%')))" +
                             "ORDER BY " + sortField + " " + sortDirection +
                             " LIMIT ? OFFSET ?;")) {
            preparedStatement.setBoolean(1, isAvailable);
            preparedStatement.setString(2, query);
            preparedStatement.setString(3, query);
            preparedStatement.setString(4, query);
            preparedStatement.setLong(5, limit);
            preparedStatement.setLong(6, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                BookMapper bookMapper = new BookMapper();
                books.add(bookMapper.extractFromResultSet(resultSet));
            }
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not find any book by given query");
        } finally {
            ConnectionManager.close(connection);
        }
        return books;
    }

    @Override
    public List<Book> findAllByIsAvailable(boolean isAvailable, Integer limit, Integer offset, String sortField,
                                           String sortDirection) {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT b.id as book_id, b.name as book_name, b.only_for_reading_hall, b.is_available, p.id AS " +
                             "publisher_id, p.name AS publisher_name, b.quantity, b.published_at, b.img_url, a.id as " +
                             "author_id, a.name as author_name, a.surname as author_surname, b.amount_of_books_taken " +
                             "FROM books AS b JOIN publishers as p ON p.id = b.publisher_id JOIN authors AS a on a.id" +
                             " = b.main_author_id WHERE b.is_available = ? ORDER BY " + sortField + " " + sortDirection +
                             " LIMIT ? OFFSET ?;")) {
            preparedStatement.setBoolean(1, isAvailable);
            preparedStatement.setLong(2, limit);
            preparedStatement.setLong(3, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                BookMapper bookMapper = new BookMapper();
                books.add(bookMapper.extractFromResultSet(resultSet));
            }
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not find any available book");
        } finally {
            ConnectionManager.close(connection);
        }
        return books;
    }

    @Override
    public Integer countAllBooks(boolean isAvailable) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT COUNT(*) FROM books WHERE is_available = ?")) {
            preparedStatement.setBoolean(1, isAvailable);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else return 0;
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not count available books");
        }
    }

    @Override
    public Integer countAllBooks(boolean isAvailable, String query) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT COUNT(*) FROM books AS b JOIN authors AS a ON b" +
                             ".main_author_id = a.id WHERE is_available = ? AND " +
                             "(UPPER(CONCAT(a.name, ' ', a.surname)) LIKE UPPER(CONCAT('%',?,'%')) " +
                             "OR UPPER(CONCAT(a.surname,' ', a.name)) LIKE UPPER(CONCAT('%',?,'%')) " +
                             "OR UPPER(b.name) LIKE UPPER(CONCAT('%',?,'%')))")) {
            preparedStatement.setBoolean(1, isAvailable);
            preparedStatement.setString(2, query);
            preparedStatement.setString(3, query);
            preparedStatement.setString(4, query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else return 0;
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not count available books with given parameters");
        }
    }

    @Override
    public Set<Author> findAdditionalAuthorsOfBook(Long bookId) {
        Set<Author> authors = new HashSet<>();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT a.id as author_id, a.name as author_name, a.surname as " +
                             "author_surname FROM authors_books JOIN authors AS a ON authors_books.author_id = a.id " +
                             "WHERE authors_books.book_id = ?")) {
            preparedStatement.setLong(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                AuthorMapper authorMapper = new AuthorMapper();
                authors.add(authorMapper.extractFromResultSet(resultSet));
            }
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not find additional authors of a book");
        }
        return authors;
    }

    @Override
    public boolean removeAuthorOfBook(Book book, Author author) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(" DELETE FROM authors_books " +
                "WHERE author_id = ? AND book_id = ?")) {
            preparedStatement.setLong(1, author.getId());
            preparedStatement.setLong(2, book.getId());
            int result = preparedStatement.executeUpdate();
            return result != 0;
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not remove book's additional author");
        }
    }

    @Override
    public boolean addAuthorOfBook(Book book, Author author) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(" INSERT INTO authors_books " +
                "(author_id, book_id) VALUES (?,?)")) {
            preparedStatement.setLong(1, author.getId());
            preparedStatement.setLong(2, book.getId());
            int result = preparedStatement.executeUpdate();
            return result != 0;
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not add book's additional author");
        }
    }


}
