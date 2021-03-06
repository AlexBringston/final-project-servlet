package ua.training.model.dao.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.PublisherDAO;
import ua.training.model.dao.mappers.PublisherMapper;
import ua.training.model.entities.Publisher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCPublisherDAO implements PublisherDAO {

    private final Connection connection;
    private final Logger logger = LogManager.getLogger(JDBCPublisherDAO.class);

    public JDBCPublisherDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Publisher entity) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("INSERT INTO publishers (name) VALUES (?);")) {
            preparedStatement.setString(1, entity.getName());
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException exception) {
            logger.log(Level.ERROR, exception.getMessage());
            throw new RuntimeException("Could not create a publisher");
        }
    }

    @Override
    public Publisher findById(Long id) {
        Publisher publisher = new Publisher();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT p.id AS publisher_id, p.name AS publisher_name FROM publishers AS" +
                             " p WHERE p.id = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            PublisherMapper publisherMapper = new PublisherMapper();
            if (rs.next()) {
                publisher = publisherMapper.extractFromResultSet(rs);
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR, exception.getMessage());
            throw new RuntimeException("Could not find a publisher by id");
        }
        return publisher;
    }

    @Override
    public List<Publisher> findAll() {
        List<Publisher> publishers = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT p.id AS publisher_id, p.name AS publisher_name FROM publishers AS p");
            PublisherMapper publisherMapper = new PublisherMapper();
            while (rs.next()) {
                publishers.add(publisherMapper.extractFromResultSet(rs));
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR, exception.getMessage());
            throw new RuntimeException("Could not find all publishers");
        }
        return publishers;
    }

    @Override
    public boolean update(Publisher entity) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("UPDATE publishers SET name = ? WHERE id = ?")) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setLong(2, entity.getId());
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException exception) {
            logger.log(Level.ERROR, exception.getMessage());
            throw new RuntimeException("Could not update a publisher");
        }
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Optional<Publisher> findByName(String name) {
        Optional<Publisher> publisher = Optional.empty();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT p.id AS publisher_id, p.name AS publisher_name FROM publishers AS" +
                             " p WHERE p.name = ?")) {
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            PublisherMapper publisherMapper = new PublisherMapper();
            if (rs.next()) {
                publisher = Optional.of(publisherMapper.extractFromResultSet(rs));
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR, exception.getMessage());
            throw new RuntimeException("Could not find a publisher by name");
        }
        return publisher;
    }
}
