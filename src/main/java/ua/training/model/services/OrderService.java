package ua.training.model.services;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.*;
import ua.training.model.dto.Page;
import ua.training.model.entities.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class OrderService {
    /**
     * DAOFactory instance to generate different DAOs
     */
    private final DAOFactory daoFactory = DAOFactory.getInstance();

    /**
     * Logger instance
     */
    private final Logger logger = LogManager.getLogger(OrderService.class);

    /**
     * Method user to create an order on given book for given user
     * @param userId - id of user
     * @param bookId - id of book
     * @param connection - connection instance
     */
    public void createOrderOnGivenBook(Long userId, Long bookId, Connection connection) {
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
            Status status = statusDAO.findStatusByName("status.pending").orElseThrow(() -> new IllegalArgumentException(
                    "There is no such status"));
            Request request = new Request(user, book, status);
            book.setAmountOfBooksTaken(book.getAmountOfBooksTaken() + 1);
            bookDao.update(book);
            requestDAO.create(request);
            connection.commit();
        } catch (SQLException exception) {
            try {
                connection.rollback();
            } catch (SQLException exception1) {
                logger.log(Level.WARN, exception1.getMessage(), exception1.getCause());
            }
            throw new RuntimeException("Could not create an order");
        }
    }

    /**
     * Method used to get all pending requests
     * @param limit - limit of instances per page
     * @param page - number of current page
     * @param sortField - field of sorting
     * @param sortDirection - direction of sorting
     * @param connection - connection instance
     * @return - page object of requests per page
     */
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

    /**
     * Method used to get books handed over to reading room
     * @param limit - limit of instances per page
     * @param page - number of current page
     * @param sortField - field of sorting
     * @param sortDirection - direction of sorting
     * @param connection - connection instance
     * @return - page object of reading room entries per page
     */
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

    /**
     * Method used to add a request as an entry to the reading room
     * @param requestId - id of request
     * @param connection - connection instance
     */
    public void addRequestToReadingRoom(Long requestId, Connection connection) {
        RequestDAO requestDAO = daoFactory.createRequestDAO(connection);
        ReadingRoomDAO readingRoomDAO = daoFactory.createReadingRoomDAO(connection);
        StatusDAO statusDAO = daoFactory.createStatusDAO(connection);
        try {
            connection.setAutoCommit(false);
            Request request = requestDAO.findById(requestId);
            ReadingRoom readingRoom = new ReadingRoom(request.getBook(), request.getUser(),
                    statusDAO.findStatusByName("status.handed.over").orElseThrow(() -> new IllegalArgumentException(
                            "There is no such status")));
            readingRoomDAO.create(readingRoom);
            request.setStatus(statusDAO.findStatusByName("status.processed").orElseThrow(() -> new IllegalArgumentException(
                    "There is no such status")));
            requestDAO.update(request);
            connection.commit();
        } catch (Exception exception) {
            try {
                connection.rollback();
            } catch (SQLException exception1) {
                logger.log(Level.WARN, exception1.getMessage(), exception1.getCause());
            }
            throw new RuntimeException("Could not add a request to reading room");
        }
    }

    /**
     * Method used to remove an entry from reading room
     * @param userId - id of user
     * @param bookId - id of book
     * @param connection - connection instance
     * @return - boolean if action is successful or not
     */
    public boolean removeTakenBookFromReadingRoom(Long userId, Long bookId, Connection connection) {
        ReadingRoomDAO readingRoomDAO = daoFactory.createReadingRoomDAO(connection);
        StatusDAO statusDAO = daoFactory.createStatusDAO(connection);
        BookDAO bookDAO = daoFactory.createBookDAO(connection);
        try {
            connection.setAutoCommit(false);
            ReadingRoom readingRoom =
                    readingRoomDAO.findBookTakenByUser(userId, bookId).orElseThrow(() -> new IllegalArgumentException(
                            "There is no such book taken by such user in reading room"));
            readingRoom.setStatus(statusDAO.findStatusByName("status.returned").orElseThrow(() -> new IllegalArgumentException(
                    "There is no such status")));
            Book book = readingRoom.getBook();
            book.setAmountOfBooksTaken(book.getAmountOfBooksTaken() - 1);
            readingRoomDAO.update(readingRoom);
            bookDAO.update(book);
            connection.commit();
            return true;
        } catch (SQLException exception) {
            try {
                connection.rollback();
            } catch (SQLException exception1) {
                logger.log(Level.WARN, exception1.getMessage(), exception1.getCause());
            }
            throw new RuntimeException("Could not remove asked request from reading room");
        }

    }

    /**
     * Method used to remove all books entries from reading room
     * @param connection - connection instance
     * @return - boolean if action is successful or not
     */
    public boolean removeAllBooksFromReadingRoom(Connection connection) {
        ReadingRoomDAO readingRoomDAO = daoFactory.createReadingRoomDAO(connection);
        StatusDAO statusDAO = daoFactory.createStatusDAO(connection);
        BookDAO bookDAO = daoFactory.createBookDAO(connection);
        List<ReadingRoom> readingRoomList =
                readingRoomDAO.findAllByStatus("status.handed.over");
        try {
            connection.setAutoCommit(false);
            for (ReadingRoom readingRoom : readingRoomList) {
                readingRoom.setStatus(statusDAO.findStatusByName("status.returned").orElseThrow(() -> new IllegalArgumentException(
                        "There is no such status")));
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
            } catch (SQLException exception1) {
                logger.log(Level.WARN, exception1.getMessage(), exception1.getCause());
            }
            throw new RuntimeException("Could not remove any request from reading room");
        }
    }

    /**
     * Method used to find a request by id
     * @param requestId - id of seeked request
     * @param connection - connection instance
     * @return - seeked request instance
     */
    public Request findRequestById(Long requestId, Connection connection) {
        RequestDAO requestDAO = daoFactory.createRequestDAO(connection);
        return requestDAO.findById(requestId);
    }

    /**
     * Method used to reject a request and update its status to 'rejected'
     * @param requestId - id of request
     * @param connection - connection instance
     */
    public void rejectRequest(Long requestId, Connection connection) {
        try {
            connection.setAutoCommit(false);
            RequestDAO requestDAO = daoFactory.createRequestDAO(connection);
            StatusDAO statusDAO = daoFactory.createStatusDAO(connection);
            BookDAO bookDAO = daoFactory.createBookDAO(connection);
            Request request = requestDAO.findById(requestId);
            request.setStatus(statusDAO.findStatusByName("status.rejected").orElseThrow(() -> new IllegalArgumentException(
                    "There is no such status")));
            Book book = request.getBook();
            book.setAmountOfBooksTaken(book.getAmountOfBooksTaken() - 1);
            bookDAO.update(book);
            requestDAO.update(request);
            connection.commit();
        } catch (SQLException exception) {
            try {
                connection.rollback();
            } catch (SQLException exception1) {
                logger.log(Level.WARN, exception1.getMessage(), exception1.getCause());
            }
            throw new RuntimeException("Could not reject a request for ordering a book");
        }
    }

    /**
     * Method used to configure a new entry which is about to be added to abonnement of user
     * @param bookId - id of book
     * @param userId - id of user
     * @param date - date of return
     * @param penalty - penalty if date of return has passed and the book wasn't returned
     * @param requestId - id of request
     * @param connection - connection instance
     */
    public void configureNewOrderForAbonnement(Long bookId, Long userId, LocalDate date, Double penalty,
                                               Long requestId, Connection connection) {
        UserDAO userDAO = daoFactory.createUserDAO(connection);
        BookDAO bookDAO = daoFactory.createBookDAO(connection);
        logger.info("Configuring new order entry to be added in abonnement");
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
            connection.commit();
        } catch (Exception exception) {
            try {
                connection.rollback();
            } catch (SQLException exception1) {
                logger.log(Level.WARN, exception1.getMessage(), exception1.getCause());
            }
            throw new RuntimeException("Could not add an entry to abonnement");
        }
    }

    /**
     * Method used to add a request entry to abonnement
     * @param abonnement - abonnement instance with info about entry
     * @param requestId - id of request
     * @param connection - connection instance
     */
    private void saveRequestToUsersAbonnement(Abonnement abonnement, Long requestId, Connection connection) {
        RequestDAO requestDAO = daoFactory.createRequestDAO(connection);
        Request request = findRequestById(requestId, connection);
        AbonnementDAO abonnementDAO = daoFactory.createAbonnementDAO(connection);
        StatusDAO statusDAO = daoFactory.createStatusDAO(connection);
        request.setStatus(statusDAO.findStatusByName("status.processed").orElseThrow(() -> new IllegalArgumentException(
                "There is no such status")));
        abonnement.setStatus(statusDAO.findStatusByName("status.handed.over").orElseThrow(() -> new IllegalArgumentException(
                "There is no such status")));
        requestDAO.update(request);
        abonnementDAO.create(abonnement);
    }

    /**
     * Method used to retrieve an abonnement of current user
     * @param userId - id of current user
     * @param status - status of seeked books
     * @param limit - limit of instances per page
     * @param page - current page number
     * @param sortField - field of sorting
     * @param sortDirection - direction of sorting
     * @param connection - connection instance
     * @return - page objects which contains list of abonnement entries
     */
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

    /**
     * Method used to change order status
     * @param action - action to apply
     * @param userId - id of user
     * @param bookId - id of book
     * @param connection - connection instance
     */
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
                    .orElseThrow(() -> new IllegalArgumentException("There is no such abonnement entry"));
            abonnement.setStatus(statusDAO.findStatusByName("status." + action).orElseThrow(() -> new IllegalArgumentException(
                    "There is no such status")));
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
                logger.log(Level.WARN, exception1.getMessage(), exception1.getCause());
            }
            throw new RuntimeException("Could not change an order status");
        }

    }

}
