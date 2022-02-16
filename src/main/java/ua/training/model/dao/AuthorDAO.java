package ua.training.model.dao;

import ua.training.model.entities.Author;

import java.util.Optional;

public interface AuthorDAO extends GenericDAO<Author> {

    Optional<Author> findByNameContainingAndSurnameContaining(String name, String surname);
}
