package ua.training.model.dao.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.ReadingRoomDAO;
import ua.training.model.dao.mappers.ReadingRoomMapper;
import ua.training.model.entities.ReadingRoom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCReadingRoomDAO implements ReadingRoomDAO {

    private final Connection connection;
    private final Logger logger = LogManager.getLogger(JDBCReadingRoomDAO.class);

    public JDBCReadingRoomDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(ReadingRoom entity) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("INSERT INTO reading_room(book_id, user_id, status_id) " +
                             "VALUES (?,?,?);")) {
            preparedStatement.setLong(1, entity.getBook().getId());
            preparedStatement.setLong(2, entity.getUser().getId());
            preparedStatement.setLong(3, entity.getStatus().getId());
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not add a book to reading room");
        }
    }

    @Override
    public ReadingRoom findById(Long id) {
        try (PreparedStatement preparedStatement =
                     connection.prepareCall("SELECT room.id as room_id, room.user_id, room.book_id, s.id AS " +
                             "status_id, " +
                             "s.name AS status_name, u.id AS user_id, u.name AS user_name, u.surname, u.username, u.password, " +
                             "u.birth_date, r.id as role_id, r.name as role_name, u.is_account_non_blocked, b.id as book_id, " +
                             "b.name as book_name, b.only_for_reading_hall, b.is_available, p.id AS publisher_id, p.name AS publisher_name, " +
                             "b.quantity, b.published_at, b.img_url, a.id AS author_id, a.name as author_name, " +
                             "a.surname as author_surname, b.amount_of_books_taken FROM reading_room AS room JOIN " +
                             "books AS b " +
                             "ON room.book_id = b.id JOIN publishers AS p ON p.id = b.publisher_id JOIN authors AS a " +
                             "ON a.id = b.main_author_id JOIN users AS u ON room.user_id = u.id JOIN statuses AS s " +
                             "ON room.status_id = s.id JOIN roles AS r ON u.role_id = r.id " +
                             "WHERE room.id = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ReadingRoomMapper readingRoomMapper = new ReadingRoomMapper();
                return readingRoomMapper.extractFromResultSet(resultSet);
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not find a book in reading room");
        }
        return null;
    }

    @Override
    public List<ReadingRoom> findAll() {
        List<ReadingRoom> readingRoomList = new ArrayList<>();
        try (PreparedStatement preparedStatement =
                     connection.prepareCall("SELECT room.id as room_id, room.user_id, room.book_id, s.id AS " +
                             "status_id, " +
                             "s.name AS status_name, u.id AS user_id, u.name AS user_name, u.surname, u.username, u.password, " +
                             "u.birth_date, r.id as role_id, r.name as role_name, u.is_account_non_blocked, b.id as book_id, " +
                             "b.name as book_name, b.only_for_reading_hall, b.is_available, p.id AS publisher_id, p.name AS publisher_name, " +
                             "b.quantity, b.published_at, b.img_url, a.id AS author_id, a.name as author_name, " +
                             "a.surname as author_surname, b.amount_of_books_taken FROM reading_room AS room JOIN " +
                             "books AS b " +
                             "ON room.book_id = b.id JOIN publishers AS p ON p.id = b.publisher_id JOIN authors AS a " +
                             "ON a.id = b.main_author_id JOIN users AS u ON room.user_id = u.id JOIN statuses AS s " +
                             "ON room.status_id = s.id JOIN roles AS r ON u.role_id = r.id;")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ReadingRoomMapper readingRoomMapper = new ReadingRoomMapper();
                readingRoomList.add(readingRoomMapper.extractFromResultSet(resultSet));
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not find books in reading room");
        }
        return readingRoomList;
    }

    @Override
    public boolean update(ReadingRoom entity) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("UPDATE reading_room SET book_id = ?, user_id = ?, status_id = ? " +
                             "WHERE id = ?;")) {
            preparedStatement.setLong(1, entity.getBook().getId());
            preparedStatement.setLong(2, entity.getUser().getId());
            preparedStatement.setLong(3, entity.getStatus().getId());
            preparedStatement.setLong(4, entity.getId());
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not update data about order in reading room");
        }
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<ReadingRoom> findBooksCurrentlyInReadingRoom(String status, Integer limit, Integer offset, String sortField, String sortDirection) {
        List<ReadingRoom> readingRoomList = new ArrayList<>();
        try (PreparedStatement preparedStatement =
                     connection.prepareCall("SELECT room.id as room_id, room.user_id, room.book_id, s.id AS " +
                             "status_id, " +
                             "s.name AS status_name, u.id AS user_id, u.name AS user_name, u.surname, u.username, u.password, " +
                             "u.birth_date, r.id as role_id, r.name as role_name, u.is_account_non_blocked, b.id as book_id, " +
                             "b.name as book_name, b.only_for_reading_hall, b.is_available, p.id AS publisher_id, p.name AS publisher_name, " +
                             "b.quantity, b.published_at, b.img_url, a.id AS author_id, a.name as author_name, " +
                             "a.surname as author_surname, b.amount_of_books_taken FROM reading_room AS room JOIN " +
                             "books AS b " +
                             "ON room.book_id = b.id JOIN publishers AS p ON p.id = b.publisher_id JOIN authors AS a " +
                             "ON a.id = b.main_author_id JOIN users AS u ON room.user_id = u.id JOIN statuses AS s " +
                             "ON room.status_id = s.id JOIN roles AS r ON u.role_id = r.id " +
                             "WHERE s.name = ? ORDER BY " + sortField + " " + sortDirection + " LIMIT ? OFFSET ?")) {
            preparedStatement.setString(1, status);
            preparedStatement.setLong(2, limit);
            preparedStatement.setLong(3, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ReadingRoomMapper readingRoomMapper = new ReadingRoomMapper();
                readingRoomList.add(readingRoomMapper.extractFromResultSet(resultSet));
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not find a book currently in reading room");
        } finally {
            ConnectionManager.close(connection);
        }
        return readingRoomList;
    }

    @Override
    public Integer countAllBooksCurrentlyInReadingRoom(String status) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT COUNT(*) FROM reading_room JOIN statuses ON reading_room" +
                             ".status_id = statuses.id WHERE statuses.name = ?")) {
            preparedStatement.setString(1, status);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else return 0;
        } catch (Exception exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not count books currently in reading room");
        }
    }

    @Override
    public Optional<ReadingRoom> findBookTakenByUser(Long userId, Long bookId) {
        Optional<ReadingRoom> readingRoom = Optional.empty();
        try (PreparedStatement preparedStatement =
                     connection.prepareCall("SELECT room.id as room_id, room.user_id, room.book_id, s.id AS " +
                             "status_id, " +
                             "s.name AS status_name, u.id AS user_id, u.name AS user_name, u.surname, u.username, u.password, " +
                             "u.birth_date, r.id as role_id, r.name as role_name, u.is_account_non_blocked, b.id as book_id, " +
                             "b.name as book_name, b.only_for_reading_hall, b.is_available, p.id AS publisher_id, p.name AS publisher_name, " +
                             "b.quantity, b.published_at, b.img_url, a.id AS author_id, a.name as author_name, " +
                             "a.surname as author_surname, b.amount_of_books_taken FROM reading_room AS room JOIN " +
                             "books AS b " +
                             "ON room.book_id = b.id JOIN publishers AS p ON p.id = b.publisher_id JOIN authors AS a " +
                             "ON a.id = b.main_author_id JOIN users AS u ON room.user_id = u.id JOIN statuses AS s " +
                             "ON room.status_id = s.id JOIN roles AS r ON u.role_id = r.id " +
                             "WHERE room.user_id = ? AND room.book_id = ?;")) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ReadingRoomMapper readingRoomMapper = new ReadingRoomMapper();
                readingRoom = Optional.of(readingRoomMapper.extractFromResultSet(resultSet));
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not find such a book taken by a given user in reading room");
        }
        return readingRoom;
    }

    @Override
    public List<ReadingRoom> findAllByStatus(String status) {
        List<ReadingRoom> readingRoomList = new ArrayList<>();
        try (PreparedStatement preparedStatement =
                     connection.prepareCall("SELECT room.id as room_id, room.user_id, room.book_id, s.id AS " +
                             "status_id, " +
                             "s.name AS status_name, u.id AS user_id, u.name AS user_name, u.surname, u.username, u.password, " +
                             "u.birth_date, r.id as role_id, r.name as role_name, u.is_account_non_blocked, b.id as book_id, " +
                             "b.name as book_name, b.only_for_reading_hall, b.is_available, p.id AS publisher_id, p.name AS publisher_name, " +
                             "b.quantity, b.published_at, b.img_url, a.id AS author_id, a.name as author_name, " +
                             "a.surname as author_surname, b.amount_of_books_taken FROM reading_room AS room JOIN " +
                             "books AS b " +
                             "ON room.book_id = b.id JOIN publishers AS p ON p.id = b.publisher_id JOIN authors AS a " +
                             "ON a.id = b.main_author_id JOIN users AS u ON room.user_id = u.id JOIN statuses AS s " +
                             "ON room.status_id = s.id JOIN roles AS r ON u.role_id = r.id " +
                             "WHERE s.name = ?;")) {
            preparedStatement.setString(1, status);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ReadingRoomMapper readingRoomMapper = new ReadingRoomMapper();
                readingRoomList.add(readingRoomMapper.extractFromResultSet(resultSet));
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not find books in reading room with given status");
        }
        return readingRoomList;
    }
}
