package ua.training.model.dao;

import ua.training.model.entities.Role;
import ua.training.model.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends GenericDAO<User> {

    Optional<User> findByUsername(String username);

    List<User> findAllByRole(Role role, Integer limit, Integer offset, String sortField, String sortDirection);

    Integer countAllByRole(String role);
}
