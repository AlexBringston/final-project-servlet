package ua.training.model.dao.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.AuthorDAO;
import ua.training.model.dao.mappers.AuthorMapper;
import ua.training.model.entities.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCAuthorDAO implements AuthorDAO {

    private final Connection connection;
    private final Logger logger = LogManager.getLogger(JDBCAuthorDAO.class);
    
    public JDBCAuthorDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Author> findByNameContainingAndSurnameContaining(String name, String surname) {
        Optional<Author> author = Optional.empty();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT a.id AS author_id, a.name AS author_name, a.surname AS " +
                             "author_surname FROM authors AS a WHERE a.name = ? AND a" +
                             ".surname = ?")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            ResultSet rs = preparedStatement.executeQuery();
            AuthorMapper authorMapper = new AuthorMapper();
            if (rs.next()) {
                author = Optional.of(authorMapper.extractFromResultSet(rs));
            }
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not find author with such name and surname");
        }
        return author;
    }

    @Override
    public boolean create(Author entity) {
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO authors (name, surname) VALUES (?,?);")) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getSurname());
            return preparedStatement.executeUpdate() != 0;
        }catch (SQLException exception){
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not create an author");
        }
    }

    @Override
    public Author findById(Long id) {
        Author author = new Author();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT a.id AS author_id, a.name AS author_name, a.surname AS " +
                             "author_surname FROM authors AS a WHERE a.name = ? AND a.id = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            AuthorMapper authorMapper = new AuthorMapper();
            if (rs.next()) {
                author = authorMapper.extractFromResultSet(rs);
            }
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not find author by given id");
        }
        return author;
    }

    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT a.id AS author_id, a.name AS author_name, a.surname AS " +
                    "author_surname FROM authors AS a");
            AuthorMapper authorMapper = new AuthorMapper();
            if (rs.next()) {
                authors.add(authorMapper.extractFromResultSet(rs));
            }
        } catch (Exception exception) {
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not find all authors");
        }
        return authors;
    }

    @Override
    public boolean update(Author entity) {
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE authors SET name = ?, surname = ? WHERE id = ?")) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getSurname());
            return preparedStatement.executeUpdate() != 0;
        }catch (SQLException exception){
            logger.log(Level.WARN, exception.getMessage());
            throw new RuntimeException("Could not update an author");
        }
    }

    @Override
    public void delete(Long id) {

    }
}
