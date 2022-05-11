package ua.training;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import ua.training.model.dao.DAOFactory;
import ua.training.model.entities.Author;
import ua.training.model.entities.Book;
import ua.training.model.entities.Publisher;
import ua.training.model.services.BookService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private DAOFactory daoFactory;

    private BookService bookService;


    @Before
    public void setupMock() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        bookService = new BookService();
    }

    @Test
    public void testFindById() throws SQLException {
        Book book = getBook();
        try (MockedStatic<DAOFactory> utils = mockStatic(DAOFactory.class)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSet.getLong("book_id")).thenReturn(1L);
            when(resultSet.getString("book_name")).thenReturn("quisque porta");
            when(resultSet.getBoolean("only_for_reading_hall")).thenReturn(false);
            when(resultSet.getBoolean("is_available")).thenReturn(true);
            when(resultSet.getLong("publisher_id")).thenReturn(1L);
            when(resultSet.getString("publisher_name")).thenReturn("publisher1");
            when(resultSet.getInt("quantity")).thenReturn(500);
            when(resultSet.getString("published_at")).thenReturn("2001-12-12");
            when(resultSet.getString("img_url")).thenReturn("https://images-na.ssl-images-amazon.com/images/I/71pX+JTU8EL" +
                    ".jpg");
            when(resultSet.getLong("author_id")).thenReturn(8L);
            when(resultSet.getString("author_name")).thenReturn("Gilemette");
            when(resultSet.getString("author_surname")).thenReturn("Sinott");
            when(resultSet.getInt("amount_of_books_taken")).thenReturn(0);
            utils.when(DAOFactory::getInstance).thenReturn(daoFactory);

            Book returnedBook = bookService.findBookById(1L, connection);
            assertEquals("The book returned was not the same as the mock", book, returnedBook);
        }

    }

    @Test(expected = RuntimeException.class)
    public void testFindByIdNotFound() {
        bookService.findBookById(1L, connection);
    }


    @Test
    public void testSaveBook() throws SQLException {
        Book book = getBook();
        book.setId(1L);
        doReturn(1).when(preparedStatement).executeUpdate();
        boolean bookCreated = bookService.saveBook(book, connection);
        assertTrue("Failed to create a book", bookCreated);
    }

    @Test
    public void testIfAuthorExists() throws SQLException {
        Book book = new Book();
        Author author = new Author(1L, "John", "Hopkins");
        book.setMainAuthor(author);
        try (MockedStatic<DAOFactory> utils = mockStatic(DAOFactory.class)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSet.getLong("author_id")).thenReturn(1L);
            when(resultSet.getString("author_name")).thenReturn("John");
            when(resultSet.getString("author_surname")).thenReturn("Hopkins");
            utils.when(DAOFactory::getInstance).thenReturn(daoFactory);

            boolean check = bookService.checkIfAuthorExists(author, connection);
            assertTrue("Author with given parameters does not exist", check);
        }
    }

    @Test
    public void testIfPublisherExists() throws SQLException {
        Book book = new Book();
        Publisher publisher = new Publisher(1L, "Books of Bliss");
        book.setPublisher(publisher);
        try (MockedStatic<DAOFactory> utils = mockStatic(DAOFactory.class)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSet.getLong("publisher_id")).thenReturn(1L);
            when(resultSet.getString("publisher_name")).thenReturn("Books of Bliss");
            utils.when(DAOFactory::getInstance).thenReturn(daoFactory);

            boolean check = bookService.checkIfPublisherExists(publisher, connection);
            assertTrue("Publisher with given parameters does not exist", check);
        }
    }

    private Book getBook() {
        return new Book.Builder().id(1L)
                .name("quisque porta")
                .onlyForReadingHall(false)
                .available(true)
                .authors(new HashSet<>())
                .publisher(new Publisher(1L, "publisher1"))
                .quantity(500)
                .publishedAt(LocalDate.of(2001, 12, 12))
                .imgUrl("https://images-na.ssl-images-amazon.com/images/I/71pX+JTU8EL.jpg")
                .mainAuthor(new Author(8L, "Gilemette", "Sinott"))
                .amountOfBooksTaken(0)
                .build();
    }
}
