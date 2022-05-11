package ua.training.model.services;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.dao.AuthorDAO;
import ua.training.model.dao.BookDAO;
import ua.training.model.dao.DAOFactory;
import ua.training.model.dao.PublisherDAO;
import ua.training.model.dao.impl.ConnectionManager;
import ua.training.model.dto.Page;
import ua.training.model.entities.Author;
import ua.training.model.entities.Book;
import ua.training.model.entities.Publisher;
import ua.training.model.utils.Constants;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookService {

    /**
     * DAOFactory instance to generate different DAOs
     */
    private final DAOFactory daoFactory = DAOFactory.getInstance();

    /**
     * Logger instance
     */
    private final Logger logger = LogManager.getLogger(BookService.class);

    /**
     * Method to save a book
     * @param book - book instance
     * @param connection - connection instance
     * @return - boolean if action is successful
     */
    public boolean saveBook(Book book, Connection connection) {
        BookDAO bookDAO = daoFactory.createBookDAO(connection);
        return bookDAO.create(book);
    }

    /**
     * Method to delete a book
     * @param book - book instance
     * @param connection - connection instance
     * @return - boolean if action if successful
     */
    public boolean deleteBook(Book book, Connection connection) {
        BookDAO bookDAO = daoFactory.createBookDAO(connection);
        book.setAvailable(false);
        System.out.println(book);
        return bookDAO.update(book);
    }

    /**
     * Method used to find books per page
     * @param query - query used to search books
     * @param limit - limit of books on page
     * @param page - number of page
     * @param sortField - field of sorting
     * @param sortDirection - direction of sorting
     * @param connection - connection instance
     * @return - Page object with list of books
     */
    public Page<Book> findBooks(String query, Integer limit, Integer page,
                                String sortField, String sortDirection, Connection connection) {
        BookDAO bookDAO = daoFactory.createBookDAO(connection);
        int totalAmountOfBooks = bookDAO.countAllBooks(true, query);
        int offset = limit * page;
        int pages = totalAmountOfBooks / limit + ((totalAmountOfBooks % limit == 0) ? 0 : 1);
        if (page > pages || page < 0) {
            throw new RuntimeException("Invalid page");
        }
        List<Book> books = bookDAO.findBooksByGivenQuery(true,query, limit, offset, sortField, sortDirection);

        return new Page.Builder<Book>()
                .objects(books)
                .firstPage(0)
                .lastPage(pages-1)
                .currentPage(page)
                .totalPages(pages-1)
                .query(query)
                .sortDirection(sortDirection)
                .sortField(sortField)
                .build();
    }

    /**
     * Method used to find a book by id
     * @param bookId - id of book
     * @param connection - connection instance
     * @return - book instance
     */
    public Book findBookById(Long bookId, Connection connection) {
        BookDAO bookDAO = daoFactory.createBookDAO(connection);
        Book book = bookDAO.findById(bookId);
        book.setAuthors(bookDAO.findAdditionalAuthorsOfBook(bookId));
        return book;
    }

    /**
     * Method to get all available books per page
     * @param limit - limit of instances per page
     * @param page - number of page
     * @param sortField - field of sorting
     * @param sortDirection - direction of sorting
     * @param connection - connection instance
     * @return - page object of books list
     */
    public Page<Book> getAllAvailableBooks(Integer limit, Integer page,
                                           String sortField, String sortDirection, Connection connection) {
        BookDAO bookDAO = daoFactory.createBookDAO(connection);
        int totalAmountOfBooks = bookDAO.countAllBooks(true);
        int offset = limit * page;
        int pages = totalAmountOfBooks / limit + ((totalAmountOfBooks % limit == 0) ? 0 : 1);
        if (page > pages || page < 0) {
            throw new RuntimeException("Invalid page");
        }
        List<Book> books = bookDAO.findAllByIsAvailable(true, limit, offset, sortField, sortDirection);

        return new Page.Builder<Book>()
                .objects(books)
                .firstPage(0)
                .lastPage(pages-1)
                .currentPage(page)
                .totalPages(pages-1)
                .sortDirection(sortDirection)
                .sortField(sortField)
                .build();
    }

    /**
     * Method used to check if author exists
     * @param author - author instance
     * @param connection - connection instance
     * @return - true if author exists, false otherwise
     */
    public boolean checkIfAuthorExists(Author author, Connection connection) {
        AuthorDAO authorDAO = daoFactory.createAuthorDAO(connection);
        return authorDAO.findByNameContainingAndSurnameContaining(author.getName(),
                author.getSurname()).isPresent();
    }

    /**
     * Method used to check if author exists
     * @param publisher - publisher instance
     * @param connection - connection instance
     * @return - true if author exists, false otherwise
     */
    public boolean checkIfPublisherExists(Publisher publisher, Connection connection) {
        PublisherDAO publisherDAO = daoFactory.createPublisherDAO(connection);
        return publisherDAO.findByName(publisher.getName()).isPresent();
    }

    /**
     * Method used to get author from database
     * @param author - author instance
     * @param connection - connection instance
     * @return - found author instance or exception is thrown
     */
    public Author getAuthor(Author author, Connection connection) {
        AuthorDAO authorDAO = daoFactory.createAuthorDAO(connection);
        return authorDAO.findByNameContainingAndSurnameContaining(author.getName(),
                author.getSurname()).orElseThrow(() -> new IllegalArgumentException("There is no such author"));
    }

    /**
     * Method used to get publisher from database
     * @param publisher - publisher instance
     * @param connection - connection instance
     * @return - found publisher instance or exception is thrown
     */
    public Publisher getPublisher(Publisher publisher, Connection connection) {
        PublisherDAO publisherDAO = daoFactory.createPublisherDAO(connection);
        return publisherDAO.findByName(publisher.getName()).orElseThrow(() -> new IllegalArgumentException("There is " +
                "no such publisher"));
    }

    /**
     * Method used to update book authors
     * @param bookId - id of book to be updated
     * @param authors - set of authors
     * @param connection - connection instance
     * @return - boolean if update was successfully performed
     */
    public boolean updateBookAuthors(Long bookId, Set<Author> authors, Connection connection) {
        BookDAO bookDAO = daoFactory.createBookDAO(connection);
        try {
            connection.setAutoCommit(false);
            Book book = bookDAO.findById(bookId);
            book.setAuthors(bookDAO.findAdditionalAuthorsOfBook(bookId));
            Set<Author> authorsToDelete = new HashSet<>(book.getAuthors());
            authorsToDelete.removeAll(authors);
            for (Author author : authorsToDelete) {
                bookDAO.removeAuthorOfBook(book,author);
            }

            Set<Author> authorsToAdd = new HashSet<>(authors);
            authorsToAdd.removeAll(book.getAuthors());
            for (Author author : authorsToAdd) {
                bookDAO.addAuthorOfBook(book,author);
            }

            connection.commit();
            ConnectionManager.close(connection);
            return true;
        } catch (Exception exception) {
            ConnectionManager.close(connection);
            try {
                connection.rollback();
            } catch (SQLException exception1) {
                logger.log(Level.WARN, exception1.getMessage(), exception1.getCause());
            }
            throw new RuntimeException("Could not update book's additional authors");
        }
    }
}