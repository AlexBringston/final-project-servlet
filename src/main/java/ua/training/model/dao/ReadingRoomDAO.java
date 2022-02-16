package ua.training.model.dao;

import ua.training.model.entities.ReadingRoom;

import java.util.List;
import java.util.Optional;

public interface ReadingRoomDAO extends GenericDAO<ReadingRoom> {

    List<ReadingRoom> findBooksCurrentlyInReadingRoom(String status, Integer limit,
                                                      Integer offset, String sortField,
                                                      String sortDirection);

    Integer countAllBooksCurrentlyInReadingRoom(String status);

    Optional<ReadingRoom> findBookTakenByUser(Long userId, Long bookId);

    List<ReadingRoom> findAllByStatus(String status);
}
