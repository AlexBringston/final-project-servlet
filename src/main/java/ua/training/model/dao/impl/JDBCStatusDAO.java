package ua.training.model.dao.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.StatusDAO;
import ua.training.model.dao.mappers.StatusMapper;
import ua.training.model.entities.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCStatusDAO implements StatusDAO {

    private final Connection connection;
    private final Logger logger = LogManager.getLogger(JDBCStatusDAO.class);

    public JDBCStatusDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Status entity) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("INSERT INTO statuses (name) VALUES (?);")) {
            preparedStatement.setString(1, entity.getName());
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException exception) {
            logger.log(Level.ERROR, exception.getMessage());
            throw new RuntimeException("Could not create status object");
        }
    }

    @Override
    public Status findById(Long id) {
        Status status = new Status();
        try (PreparedStatement preparedStatement =
                     connection.prepareCall("SELECT s.id AS status_id, s.name AS status_name FROM statuses AS" +
                             " s WHERE s.id = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            StatusMapper statusMapper = new StatusMapper();
            if (rs.next()) {
                status = statusMapper.extractFromResultSet(rs);
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR, exception.getMessage());
            throw new RuntimeException("Could not find status with given id");
        }
        return status;
    }

    @Override
    public List<Status> findAll() {
        List<Status> statuses = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT s.id AS status_id, s.name AS status_name FROM statuses AS " +
                    "s");
            StatusMapper statusMapper = new StatusMapper();
            while (rs.next()) {
                statuses.add(statusMapper.extractFromResultSet(rs));
            }
        } catch (Exception exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not find statuses");
        }
        return statuses;
    }

    @Override
    public boolean update(Status entity) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("UPDATE statuses SET name = ? WHERE id = ?;")) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setLong(2, entity.getId());
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not update status name");
        }
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Optional<Status> findStatusByName(String name) {
        Optional<Status> optionalStatus = Optional.empty();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT statuses.id as status_id, statuses.name as status_name FROM " +
                             "statuses WHERE statuses.name = ?")) {
            preparedStatement.setString(1, name);
            StatusMapper statusMapper = new StatusMapper();
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                optionalStatus = Optional.of(statusMapper.extractFromResultSet(resultSet));
            }
        } catch (SQLException exception) {
            logger.log(Level.ERROR,exception.getMessage());
            throw new RuntimeException("Could not find status name");
        }
        return optionalStatus;
    }
}
