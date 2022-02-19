package ua.training.model.dao.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.AbonnementDAO;
import ua.training.model.dao.mappers.AbonnementMapper;
import ua.training.model.entities.Abonnement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCAbonnementDAO implements AbonnementDAO {

    private final Connection connection;
    private final Logger logger = LogManager.getLogger(JDBCAbonnementDAO.class);

    public JDBCAbonnementDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Abonnement entity) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("INSERT INTO abonnements(user_id, book_id, penalty, return_date, " +
                             "status_id) VALUES (?,?,?,?,?)")) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, entity.getUser().getId());
            preparedStatement.setLong(2, entity.getBook().getId());
            preparedStatement.setDouble(3, entity.getPenalty());
            preparedStatement.setDate(4, Date.valueOf(entity.getReturnDate()));
            preparedStatement.setLong(5, entity.getStatus().getId());
            int executeUpdate = preparedStatement.executeUpdate();
            connection.commit();
            return executeUpdate != 0;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            logger.log(Level.WARN, e.getMessage());
            throw new RuntimeException("Could not create an abonnement entry");
        }
    }

    @Override
    public Abonnement findById(Long id) {
        Abonnement abonnement = new Abonnement();
        try (PreparedStatement preparedStatement =
                     connection.prepareCall("SELECT ab.id AS abonnement_id, ab.user_id, ab.book_id, ab.penalty, ab" +
                             ".return_date, s.id as status_id, " +
                             "s.name as status_name, u.id AS user_id, u.name AS user_name, u.surname, u.username, u.password, " +
                             "u.birth_date, r.id as role_id, r.name as role_name, u.is_account_non_blocked, " +
                             "b.id as book_id, b.name as book_name, " +
                             "b.only_for_reading_hall, b.is_available, p.id AS publisher_id, " +
                             "p.name AS publisher_name, " +
                             "b.quantity, b.published_at, b.img_url, a.id AS author_id, a.name as author_name, " +
                             "a.surname as author_surname, b.amount_of_books_taken FROM abonnements AS ab JOIN books AS b " +
                             "ON ab.book_id = b.id JOIN publishers AS p ON p.id = b.publisher_id JOIN authors AS a " +
                             "ON a.id = b.main_author_id JOIN users AS u ON ab.user_id = u.id JOIN statuses AS s " +
                             "ON ab.status_id = s.id JOIN roles AS r ON u.role_id = r.id " +
                             "WHERE ab.id = ?")) {
            preparedStatement.setLong(1, id);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                AbonnementMapper abonnementMapper = new AbonnementMapper();
                abonnement = abonnementMapper.extractFromResultSet(resultSet);
            }
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not abonnement entry by its id");
        }
        return abonnement;
    }

    @Override
    public List<Abonnement> findAll() {
        List<Abonnement> abonnements = new ArrayList<>();
        try (Statement statement =
                     connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT ab.id AS abonnement_id, ab.user_id, ab.book_id, ab" +
                    ".penalty, ab.return_date, s.id as status_id, " +
                    "s.name as status_name, u.id AS user_id, u.name AS user_name, u.surname, u.username, u.password, " +
                    "u.birth_date, r.id as role_id, r.name as role_name, u.is_account_non_blocked, " +
                    "b.id as book_id, b.name as book_name, " +
                    "b.only_for_reading_hall, b.is_available, p.id AS publisher_id, " +
                    "p.name AS publisher_name, " +
                    "b.quantity, b.published_at, b.img_url, a.id AS author_id, a.name as author_name, " +
                    "a.surname as author_surname, b.amount_of_books_taken FROM abonnements AS ab JOIN books AS b " +
                    "ON ab.book_id = b.id JOIN publishers AS p ON p.id = b.publisher_id JOIN authors AS a " +
                    "ON a.id = b.main_author_id JOIN users AS u ON ab.user_id = u.id JOIN statuses AS s " +
                    "ON ab.status_id = s.id JOIN roles AS r ON u.role_id = r.id;");
            while (resultSet.next()) {
                AbonnementMapper abonnementMapper = new AbonnementMapper();
                abonnements.add(abonnementMapper.extractFromResultSet(resultSet));
            }
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not create an abonnement entry");
        } finally {
            ConnectionManager.close(connection);
        }
        return abonnements;
    }

    @Override
    public boolean update(Abonnement entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE abonnements SET penalty = ?, " +
                "return_date = ?, status_id = ?, user_id = ?, book_id = ? WHERE id = ?;")) {
            connection.setAutoCommit(false);
            preparedStatement.setDouble(1, entity.getPenalty());
            preparedStatement.setDate(2, Date.valueOf(entity.getReturnDate()));
            preparedStatement.setLong(3, entity.getStatus().getId());
            preparedStatement.setLong(4, entity.getUser().getId());
            preparedStatement.setLong(5, entity.getBook().getId());
            preparedStatement.setLong(6, entity.getId());
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result != 0;
        } catch (SQLException exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not update an abonnement entry");
        }
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Abonnement> findAbonnementsByUserIdAndStatusNameContaining(Long userId, String status,
                                                                           Integer limit, Integer offset,
                                                                           String sortField, String sortDirection) {
        List<Abonnement> abonnements = new ArrayList<>();
        try (PreparedStatement preparedStatement =
                     connection.prepareCall("SELECT ab.id AS abonnement_id, ab.user_id, ab.book_id, ab.penalty, ab" +
                             ".return_date, s.id AS status_id, " +
                             "s.name AS status_name, u.id AS user_id, u.name AS user_name, u.surname, u.username, u" +
                             ".password, " +
                             "u.birth_date, r.id AS role_id, r.name AS role_name, u.is_account_non_blocked, " +
                             "b.id AS book_id, b.name AS book_name, " +
                             "b.only_for_reading_hall, b.is_available, p.id AS publisher_id, " +
                             "p.name AS publisher_name, " +
                             "b.quantity, b.published_at, b.img_url, a.id AS author_id, a.name AS author_name, " +
                             "a.surname as author_surname, b.amount_of_books_taken FROM abonnements AS ab JOIN books AS b " +
                             "ON ab.book_id = b.id JOIN publishers AS p ON p.id = b.publisher_id JOIN authors AS a " +
                             "ON a.id = b.main_author_id JOIN users AS u ON ab.user_id = u.id JOIN statuses AS s " +
                             "ON ab.status_id = s.id JOIN roles AS r ON u.role_id = r.id " +
                             "WHERE ab.user_id = ? AND s.name LIKE CONCAT('%',?,'%') ORDER BY " + sortField + " " + sortDirection +
                             " LIMIT ? OFFSET ?;")) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, status);
            preparedStatement.setLong(3, limit);
            preparedStatement.setLong(4, offset);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                AbonnementMapper abonnementMapper = new AbonnementMapper();
                abonnements.add(abonnementMapper.extractFromResultSet(resultSet));
            }
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not find abonnements entry");
        } finally {
            ConnectionManager.close(connection);
        }
        return abonnements;
    }

    @Override
    public Integer countAllBooksOfUser(Long userId) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT COUNT(*) FROM abonnements WHERE user_id = ?")) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else return 0;
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not count abonnements entries");
        }
    }

    @Override
    public Optional<Abonnement> findAbonnementByUserIdAndBookId(Long userId, Long bookId) {
        Optional<Abonnement> optionalAbonnement = Optional.empty();
        try (PreparedStatement preparedStatement =
                     connection.prepareCall("SELECT ab.id AS abonnement_id, ab.user_id, ab.book_id, ab.penalty, ab" +
                             ".return_date, s.id as status_id, " +
                             "s.name as status_name, u.id AS user_id, u.name AS user_name, u.surname, u.username, u.password, " +
                             "u.birth_date, r.id as role_id, r.name as role_name, u.is_account_non_blocked, " +
                             "b.id as book_id, b.name as book_name, " +
                             "b.only_for_reading_hall, b.is_available, p.id AS publisher_id, " +
                             "p.name AS publisher_name, " +
                             "b.quantity, b.published_at, b.img_url, a.id AS author_id, a.name as author_name, " +
                             "a.surname as author_surname, b.amount_of_books_taken FROM abonnements AS ab JOIN books AS b " +
                             "ON ab.book_id = b.id JOIN publishers AS p ON p.id = b.publisher_id JOIN authors AS a " +
                             "ON a.id = b.main_author_id JOIN users AS u ON ab.user_id = u.id JOIN statuses AS s " +
                             "ON ab.status_id = s.id JOIN roles AS r ON u.role_id = r.id " +
                             "WHERE ab.user_id = ? AND ab.book_id = ?")) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, bookId);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                AbonnementMapper abonnementMapper = new AbonnementMapper();
                optionalAbonnement = Optional.of(abonnementMapper.extractFromResultSet(resultSet));
            }
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not abonnement entry by user id and book id");
        }
        return optionalAbonnement;
    }

}
