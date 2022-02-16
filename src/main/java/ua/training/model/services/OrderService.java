package ua.training.model.services;

import ua.training.model.dao.*;
import ua.training.model.dto.Page;
import ua.training.model.entities.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class OrderService {

    private final DAOFactory daoFactory = DAOFactory.getInstance();

    public void createOrderOnGivenBook(Long userId, Long bookId, Connection connection) throws SQLException {
        try {
            connection.setAutoCommit(false);
            BookDAO bookDao = daoFactory.createBookDAO(connection);
            UserDAO userDAO = daoFactory.createUserDAO(connection);
            StatusDAO statusDAO = daoFactory.createStatusDAO(connection);
            RequestDAO requestDAO = daoFactory.createRequestDAO(connection);
            Book book = bookDao.findById(bookId);
            User user = userDAO.findById(userId);
            if (book.getQuantity().equals(0) || book.getAmountOfBooksTaken().equals(book.getQuantity())) {
                throw new RuntimeException("The book is not available to be given");
            }
            Status status = statusDAO.findStatusByName("status.pending").orElseThrow(RuntimeException::new);
            Request request = new Request(user, book, status);
            book.setAmountOfBooksTaken(book.getAmountOfBooksTaken() + 1);
            bookDao.update(book);
            requestDAO.create(request);
            connection.commit();
        } catch (SQLException exception) {
            try {
                connection.rollback();
            } catch (SQLException exception1) {

            }
        }
    }

    public Page<Request> getAllPendingRequests(Integer limit, Integer page, String sortField,
                                               String sortDirection, Connection connection) {
        RequestDAO requestDAO = daoFactory.createRequestDAO(connection);
        int totalAmountOfRequests = requestDAO.countAllRequestsWithStatus("status.pending");
        int offset = limit * page;
        int pages = totalAmountOfRequests / limit + ((totalAmountOfRequests % limit == 0) ? 0 : 1);
        if (page > pages || page < 0) {
            throw new RuntimeException("Page number is not valid");
        }
        List<Request> requests = requestDAO.findAllByStatusName("status.pending", limit, offset, sortField, sortDirection);

        return new Page.Builder<Request>()
                .objects(requests)
                .firstPage(0)
                .lastPage(pages - 1)
                .currentPage(page)
                .totalPages(pages - 1)
                .sortDirection(sortDirection)
                .sortField(sortField)
                .build();
    }

    public Page<ReadingRoom> getBooksHandedOverToReadingRoom(Integer limit, Integer page,
                                                             String sortField, String sortDirection,
                                                             Connection connection) {
        ReadingRoomDAO readingRoomDAO = daoFactory.createReadingRoomDAO(connection);
        Integer totalBooksHandedOver = readingRoomDAO.countAllBooksCurrentlyInReadingRoom("status.handed.over");
        int offset = limit * page;
        int pages = totalBooksHandedOver / limit + ((totalBooksHandedOver % limit == 0) ? 0 : 1);
        if (page < 0 || page > pages) {
            throw new RuntimeException("Page number is not valid");
        }

        List<ReadingRoom> readingRoomBooks = readingRoomDAO.findBooksCurrentlyInReadingRoom("status.handed.over",
                limit, offset, sortField, sortDirection);

        return new Page.Builder<ReadingRoom>()
                .objects(readingRoomBooks)
                .firstPage(0)
                .lastPage(pages - 1)
                .currentPage(page)
                .totalPages(pages - 1)
                .sortField(sortField)
                .sortDirection(sortDirection)
                .build();
    }

    public void addRequestToReadingRoom(Long requestId, Connection connection) {
        RequestDAO requestDAO = daoFactory.createRequestDAO(connection);
        ReadingRoomDAO readingRoomDAO = daoFactory.createReadingRoomDAO(connection);
        StatusDAO statusDAO = daoFactory.createStatusDAO(connection);
        try {
            connection.setAutoCommit(false);
            Request request = requestDAO.findById(requestId);
            ReadingRoom readingRoom = new ReadingRoom(request.getBook(), request.getUser(),
                    statusDAO.findStatusByName("status.handed.over").orElseThrow(RuntimeException::new));
            readingRoomDAO.create(readingRoom);
            request.setStatus(statusDAO.findStatusByName("status.processed").orElseThrow(RuntimeException::new));
            requestDAO.update(request);
            connection.commit();
        } catch (Exception exception) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean removeTakenBookFromReadingRoom(Long userId, Long bookId, Connection connection) {
        ReadingRoomDAO readingRoomDAO = daoFactory.createReadingRoomDAO(connection);
        StatusDAO statusDAO = daoFactory.createStatusDAO(connection);
        BookDAO bookDAO = daoFactory.createBookDAO(connection);
        try {
            connection.setAutoCommit(false);
            ReadingRoom readingRoom = readingRoomDAO.findBookTakenByUser(userId, bookId).orElseThrow(RuntimeException::new);
            readingRoom.setStatus(statusDAO.findStatusByName("status.returned").orElseThrow(RuntimeException::new));
            Book book = readingRoom.getBook();
            book.setAmountOfBooksTaken(book.getAmountOfBooksTaken() - 1);
            readingRoomDAO.update(readingRoom);
            bookDAO.update(book);
            connection.commit();
            return true;
        } catch (SQLException exception) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

    }

    public boolean removeAllBooksFromReadingRoom(Connection connection) {
        ReadingRoomDAO readingRoomDAO = daoFactory.createReadingRoomDAO(connection);
        StatusDAO statusDAO = daoFactory.createStatusDAO(connection);
        BookDAO bookDAO = daoFactory.createBookDAO(connection);
        List<ReadingRoom> readingRoomList =
                readingRoomDAO.findAllByStatus("status.handed.over");
        try {
            connection.setAutoCommit(false);
            for (ReadingRoom readingRoom : readingRoomList) {
                readingRoom.setStatus(statusDAO.findStatusByName("status.returned").orElseThrow(RuntimeException::new));
                Book book = readingRoom.getBook();
                book.setAmountOfBooksTaken(book.getAmountOfBooksTaken() - 1);
                bookDAO.update(book);
                readingRoomDAO.update(readingRoom);
            }
            connection.commit();
            return true;
        } catch (SQLException exception) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public Request findRequestById(Long requestId, Connection connection) {
        RequestDAO requestDAO = daoFactory.createRequestDAO(connection);
        return requestDAO.findById(requestId);
    }

    public void rejectRequest(Long requestId, Connection connection) {
        try {
            connection.setAutoCommit(false);
            RequestDAO requestDAO = daoFactory.createRequestDAO(connection);
            StatusDAO statusDAO = daoFactory.createStatusDAO(connection);
            BookDAO bookDAO = daoFactory.createBookDAO(connection);
            Request request = requestDAO.findById(requestId);
            request.setStatus(statusDAO.findStatusByName("status.rejected").orElseThrow(RuntimeException::new));
            Book book = request.getBook();
            book.setAmountOfBooksTaken(book.getAmountOfBooksTaken() - 1);
            bookDAO.update(book);
            requestDAO.update(request);
            connection.commit();
        } catch (SQLException exception) {
            try {
                connection.rollback();
            } catch (SQLException e) {
            }
        }
    }


    public void configureNewOrderForAbonnement(Long bookId, Long userId, LocalDate date, Double penalty,
                                               Long requestId, Connection connection) {
        UserDAO userDAO = daoFactory.createUserDAO(connection);
        BookDAO bookDAO = daoFactory.createBookDAO(connection);
        System.out.println("CONFIGURE");
        try {
            connection.setAutoCommit(false);
            User user = userDAO.findById(userId);
            Book book = bookDAO.findById(bookId);
            if (book.isOnlyForReadingHall() || book.getQuantity().equals(0) || book.getAmountOfBooksTaken().equals(book.getQuantity())) {
                throw new RuntimeException("The book is not available to be given");
            }
            Abonnement abonnement = new Abonnement();
            abonnement.setBook(book);
            abonnement.setUser(user);
            abonnement.setReturnDate(date);
            abonnement.setPenalty(penalty);
            saveRequestToUsersAbonnement(abonnement, requestId, connection);
            book.setAmountOfBooksTaken(book.getAmountOfBooksTaken() + 1);
            bookDAO.update(book);
            System.out.println(book);
            connection.commit();
        } catch (Exception exception) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private void saveRequestToUsersAbonnement(Abonnement abonnement, Long requestId, Connection connection) {
        RequestDAO requestDAO = daoFactory.createRequestDAO(connection);
        Request request = findRequestById(requestId, connection);
        AbonnementDAO abonnementDAO = daoFactory.createAbonnementDAO(connection);
        StatusDAO statusDAO = daoFactory.createStatusDAO(connection);
        request.setStatus(statusDAO.findStatusByName("status.processed").orElseThrow(RuntimeException::new));
        abonnement.setStatus(statusDAO.findStatusByName("status.handed.over").orElseThrow(RuntimeException::new));
        System.out.println(abonnement);
        System.out.println(request);
        requestDAO.update(request);
        abonnementDAO.create(abonnement);
    }

    public Page<Abonnement> findCurrentUserAbonnement(Long userId, String status, Integer limit, Integer page,
                                                      String sortField, String sortDirection, Connection connection) {
        AbonnementDAO abonnementDAO = daoFactory.createAbonnementDAO(connection);
        int totalAmountOfAbonnements = abonnementDAO.countAllBooksOfUser(userId);
        int offset = limit * page;
        int pages = totalAmountOfAbonnements / limit + ((totalAmountOfAbonnements % limit == 0) ? 0 : 1);
        if (page > pages || page < 0) {
            throw new RuntimeException("Invalid page");
        }
        List<Abonnement> userBooks = abonnementDAO.findAbonnementsByUserIdAndStatusNameContaining(userId,
                status, limit, offset, sortField, sortDirection);

        return new Page.Builder<Abonnement>()
                .objects(userBooks)
                .firstPage(0)
                .lastPage(pages - 1)
                .currentPage(page)
                .totalPages(pages - 1)
                .sortDirection(sortDirection)
                .sortField(sortField)
                .build();
    }

    public void changeOrderStatus(String action, Long userId, Long bookId, Connection connection) {
        System.out.println(action);
        System.out.println(userId);
        System.out.println(bookId);
        AbonnementDAO abonnementDAO = daoFactory.createAbonnementDAO(connection);
        StatusDAO statusDAO = daoFactory.createStatusDAO(connection);
        BookDAO bookDAO = daoFactory.createBookDAO(connection);
        try {
            connection.setAutoCommit(false);
            Abonnement abonnement = abonnementDAO.findAbonnementByUserIdAndBookId(userId, bookId)
                    .orElseThrow(RuntimeException::new);
            abonnement.setStatus(statusDAO.findStatusByName("status." + action).orElseThrow(RuntimeException::new));
            if (abonnement.getStatus().getName().equals("status.returned")) {
                Book book = abonnement.getBook();
                book.setAmountOfBooksTaken(book.getAmountOfBooksTaken() - 1);
                bookDAO.update(book);
            }
            System.out.println(abonnement);
            abonnementDAO.update(abonnement);
            connection.commit();
        } catch (SQLException exception) {
            try {
                connection.rollback();
            } catch (SQLException exception1) {
                System.out.println(exception1.getMessage());
            }
        }

    }

}
