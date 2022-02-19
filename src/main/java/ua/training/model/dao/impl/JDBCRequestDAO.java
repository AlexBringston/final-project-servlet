package ua.training.model.dao.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.RequestDAO;
import ua.training.model.dao.mappers.RequestMapper;
import ua.training.model.entities.Request;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCRequestDAO implements RequestDAO {

    private final Connection connection;
    private final Logger logger = LogManager.getLogger(JDBCRequestDAO.class);

    public JDBCRequestDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Request entity) {
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO requests(book_id, user_id, status_id) VALUES (?,?,?);")) {
            preparedStatement.setLong(1, entity.getBook().getId());
            preparedStatement.setLong(2, entity.getUser().getId());
            preparedStatement.setLong(3, entity.getStatus().getId());
            return preparedStatement.executeUpdate() != 0;
        }catch (SQLException exception){
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not create a request");
        }
    }

    @Override
    public Request findById(Long id) {
        try (PreparedStatement preparedStatement =
                     connection.prepareCall("SELECT req.id, req.user_id, req.book_id, s.id AS status_id, " +
                             "s.name AS status_name, u.id AS user_id, u.name AS user_name, u.surname, u.username, u.password, " +
                             "u.birth_date, r.id as role_id, r.name as role_name, u.is_account_non_blocked, b.id as book_id, " +
                             "b.name as book_name, b.only_for_reading_hall, b.is_available, p.id AS publisher_id, p.name AS publisher_name, " +
                             "b.quantity, b.published_at, b.img_url, a.id AS author_id, a.name as author_name, " +
                             "a.surname as author_surname, b.amount_of_books_taken FROM requests AS req JOIN books AS b "+
                             "ON req.book_id = b.id JOIN publishers AS p ON p.id = b.publisher_id JOIN authors AS a " +
                             "ON a.id = b.main_author_id JOIN users AS u ON req.user_id = u.id JOIN statuses AS s " +
                             "ON req.status_id = s.id JOIN roles AS r ON u.role_id = r.id " +
                             "WHERE req.id = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                RequestMapper requestMapper = new RequestMapper();
                return requestMapper.extractFromResultSet(resultSet);
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not find books in reading room with given status");
        }
        return null;
    }

    @Override
    public List<Request> findAll() {
        List<Request> requests = new ArrayList<>();
        try (Statement statement =
                     connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT req.id, req.user_id, req.book_id, s.id AS status_id, " +
                    "s.name AS status_name, u.id AS user_id, u.name AS user_name, u.surname, u.username, u.password, " +
                    "u.birth_date, r.id as role_id, r.name as role_name, u.is_account_non_blocked, b.id as book_id, " +
                    "b.name as book_name, b.only_for_reading_hall, b.is_available, p.id AS publisher_id, p.name AS publisher_name, " +
                    "b.quantity, b.published_at, b.img_url, a.id AS author_id, a.name as author_name, " +
                    "a.surname as author_surname, b.amount_of_books_taken FROM requests AS req JOIN books AS b "+
                    "ON req.book_id = b.id JOIN publishers AS p ON p.id = b.publisher_id JOIN authors AS a " +
                    "ON a.id = b.main_author_id JOIN users AS u ON req.user_id = u.id JOIN statuses AS s " +
                    "ON req.status_id = s.id JOIN roles AS r ON u.role_id = r.id;");
            while (resultSet.next()) {
                RequestMapper requestMapper = new RequestMapper();
                requests.add(requestMapper.extractFromResultSet(resultSet));
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not find all requests");
        }
        finally {
            ConnectionManager.close(connection);
        }
        return requests;
    }

    @Override
    public boolean update(Request entity) {
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE requests SET book_id = ?, user_id = ?, status_id = ? " +
                            "WHERE id = ?")) {
            preparedStatement.setLong(1, entity.getBook().getId());
            preparedStatement.setLong(2, entity.getUser().getId());
            preparedStatement.setLong(3, entity.getStatus().getId());
            preparedStatement.setLong(4, entity.getId());
            return preparedStatement.executeUpdate() != 0;
        }catch (SQLException exception){
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not update a request");
        }
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Request> findAllByStatusName(String status, Integer limit, Integer offset, String sortField, String sortDirection) {
        List<Request> requests = new ArrayList<>();
        try (PreparedStatement preparedStatement =
                     connection.prepareCall("SELECT req.id, req.user_id, req.book_id, s.id AS status_id, " +
                             "s.name AS status_name, u.id AS user_id, u.name AS user_name, u.surname, u.username, u.password, " +
                             "u.birth_date, r.id as role_id, r.name as role_name, u.is_account_non_blocked, b.id as book_id, " +
                             "b.name as book_name, b.only_for_reading_hall, b.is_available, p.id AS publisher_id, p.name AS publisher_name, " +
                             "b.quantity, b.published_at, b.img_url, a.id AS author_id, a.name as author_name, " +
                             "a.surname as author_surname, b.amount_of_books_taken FROM requests AS req JOIN books AS b "+
                             "ON req.book_id = b.id JOIN publishers AS p ON p.id = b.publisher_id JOIN authors AS a " +
                             "ON a.id = b.main_author_id JOIN users AS u ON req.user_id = u.id JOIN statuses AS s " +
                             "ON req.status_id = s.id JOIN roles AS r ON u.role_id = r.id " +
                             "WHERE s.name = ? ORDER BY " + sortField + " " + sortDirection +
                             " LIMIT ? OFFSET ?;")) {
            preparedStatement.setString(1, status);
            preparedStatement.setLong(2, limit);
            preparedStatement.setLong(3, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                RequestMapper requestMapper = new RequestMapper();
                requests.add(requestMapper.extractFromResultSet(resultSet));
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not find any request with given status");
        }
        finally {
            ConnectionManager.close(connection);
        }
        return requests;
    }

    @Override
    public Integer countAllRequestsWithStatus(String status) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT COUNT(*) FROM requests JOIN statuses ON requests.status_id =" +
                             " statuses.id WHERE statuses.name = ?")) {
            preparedStatement.setString(1, status);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            else return 0;
        } catch (Exception exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not count requests with given status name");
        }
    }
}
