package ua.training.model.dao;

import ua.training.model.entities.Author;
import ua.training.model.entities.Book;

import java.util.List;
import java.util.Set;

public interface BookDAO extends GenericDAO<Book> {

    List<Book> findBooksByGivenQuery(boolean isAvailable, String query, Integer limit, Integer page,
                                     String sortField, String sortDirection);

    List<Book> findAllByIsAvailable(boolean isAvailable, Integer limit, Integer offset,
                                    String sortField, String sortDirection);

    Integer countAllBooks(boolean isAvailable);

    Integer countAllBooks(boolean isAvailable, String query);

    Set<Author> findAdditionalAuthorsOfBook(Long bookId);

    boolean removeAuthorOfBook(Book book, Author author);

    boolean addAuthorOfBook(Book book, Author author);
}
