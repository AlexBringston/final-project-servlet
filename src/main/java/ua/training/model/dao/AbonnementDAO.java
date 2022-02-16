package ua.training.model.dao;

import ua.training.model.entities.Abonnement;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface AbonnementDAO extends GenericDAO<Abonnement> {

    List<Abonnement> findAbonnementsByUserIdAndStatusNameContaining(Long usedId, String status, Integer limit,
                                                                    Integer offset, String sortField,
                                                                    String sortDirection);

    Integer countAllBooksOfUser(Long userId);

    Optional<Abonnement> findAbonnementByUserIdAndBookId(Long userId, Long bookId);

}
