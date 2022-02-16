package ua.training.model.dao;

import ua.training.model.entities.Status;

import java.util.Optional;

public interface StatusDAO extends GenericDAO<Status> {
    Optional<Status> findStatusByName(String name);
}
