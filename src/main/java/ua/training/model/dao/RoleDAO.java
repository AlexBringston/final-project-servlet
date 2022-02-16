package ua.training.model.dao;

import ua.training.model.entities.Role;

import java.util.Optional;

public interface RoleDAO extends GenericDAO<Role> {

    Optional<Role> findByName(String name);
}
