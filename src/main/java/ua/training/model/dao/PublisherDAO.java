package ua.training.model.dao;

import ua.training.model.entities.Publisher;

import java.util.Optional;

public interface PublisherDAO extends GenericDAO<Publisher> {

    Optional<Publisher> findByName(String name);
}
