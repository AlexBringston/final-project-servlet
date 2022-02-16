package ua.training.model.dao;

import ua.training.model.entities.Request;

import java.util.List;

public interface RequestDAO extends GenericDAO<Request>{

    List<Request> findAllByStatusName(String status, Integer limit, Integer offset, String sortField,
                                      String sortDirection);

    Integer countAllRequestsWithStatus(String status);
}
