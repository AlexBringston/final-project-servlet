package ua.training.model.services;


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

    private final DAOFactory daoFactory = DAOFactory.getInstance();


    public boolean saveBook(Book book, Connection connection) {
        BookDAO bookDAO = daoFactory.createBookDAO(connection);
        return bookDAO.create(book);
    }

    public boolean deleteBook(Book book, Connection connection) {
        BookDAO bookDAO = daoFactory.createBookDAO(connection);
        book.setAvailable(false);
        System.out.println(book);
        return bookDAO.update(book);
    }

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

    public Book findBookById(Long bookId, Connection connection) {
        BookDAO bookDAO = daoFactory.createBookDAO(connection);
        Book book = bookDAO.findById(bookId);
        book.setAuthors(bookDAO.findAdditionalAuthorsOfBook(bookId));
        return book;
    }

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

    public boolean checkIfAuthorExists(Author author, Connection connection) {
        AuthorDAO authorDAO = daoFactory.createAuthorDAO(connection);
        return authorDAO.findByNameContainingAndSurnameContaining(author.getName(),
                author.getSurname()).isPresent();
    }

    public boolean checkIfPublisherExists(Publisher publisher, Connection connection) {
        PublisherDAO publisherDAO = daoFactory.createPublisherDAO(connection);
        return publisherDAO.findByName(publisher.getName()).isPresent();
    }

    public Author getAuthor(Author author, Connection connection) {
        AuthorDAO authorDAO = daoFactory.createAuthorDAO(connection);
        return authorDAO.findByNameContainingAndSurnameContaining(author.getName(),
                author.getSurname()).orElseThrow(RuntimeException::new);
    }

    public Publisher getPublisher(Publisher publisher, Connection connection) {
        PublisherDAO publisherDAO = daoFactory.createPublisherDAO(connection);
        return publisherDAO.findByName(publisher.getName()).orElseThrow(RuntimeException::new);
    }

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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}