package ua.training.model.dao;

import java.util.List;

public interface GenericDAO<T>{

    boolean create(T entity);
    T findById(Long id);
    List<T> findAll();
    boolean update(T entity);
    void delete(Long id);
}
