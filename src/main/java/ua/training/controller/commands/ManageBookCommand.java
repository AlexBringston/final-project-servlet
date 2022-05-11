package ua.training.controller.commands;

import ua.training.model.dao.impl.ConnectionManager;
import ua.training.model.entities.Author;
import ua.training.model.entities.Book;
import ua.training.model.entities.Publisher;
import ua.training.model.services.BookService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static ua.training.model.utils.RegEx.*;

/**
 * Command used to manage all operations available for admin to perform with books
 */
public class ManageBookCommand implements Command {

    private final BookService bookService;

    public ManageBookCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public String execute(HttpServletRequest request) throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        System.out.println(request.getParameter("action"));
        switch (request.getParameter("action")) {
            case "add":
                return addBook(request, connection);
            case "update":
                try {
                    Long bookId = Long.parseLong(request.getParameter("bookId"));
                    return updateBook(bookId, request, connection);
                } catch (NumberFormatException exception) {
                    return "redirect:/admin/books";
                }
            case "updateAuthors":
                try {
                    Long bookId = Long.parseLong(request.getParameter("bookId"));
                    return updateBookAuthors(bookId, request, connection);
                } catch (NumberFormatException exception) {
                    return "redirect:/admin/books";
                }
            case "delete":
                try {
                    Long bookId = Long.parseLong(request.getParameter("bookId"));
                    return deleteBook(bookId, connection);
                } catch (NumberFormatException exception) {
                    return "redirect:/admin/books";
                }
            default:
                break;
        }
        ConnectionManager.close(connection);
        try {
            System.out.println(connection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/books";
    }

    private String addBook(HttpServletRequest request, Connection connection) {
        if (request.getMethod().equals("GET")) {
            return "/WEB-INF/admin/adminNewBook.jsp";
        }

        Book book = buildBookWithReceivedParameters(request, connection);
        if (book == null) {
            return "/WEB-INF/admin/adminNewBook.jsp";
        }
        System.out.println(book);
        bookService.saveBook(book, connection);
        ConnectionManager.close(connection);
        return "redirect:/admin/books";
    }

    private String updateBook(Long bookId, HttpServletRequest request, Connection connection) {
        if (request.getMethod().equals("GET")) {
            request.setAttribute("book", bookService.findBookById(bookId, connection));
            return "/WEB-INF/admin/adminEditBook.jsp";
        }
        Book book = buildBookWithReceivedParameters(request, connection);
        if (book == null) {
            return "/WEB-INF/admin/adminEditBook.jsp";
        }
        book.setId(bookId);
        System.out.println(book);
        return "redirect:/admin/books";
    }

    private String updateBookAuthors(Long bookId, HttpServletRequest request, Connection connection) {
        if (request.getMethod().equals("GET")) {
            Book bookById = bookService.findBookById(bookId, connection);
            request.setAttribute("book", bookById);
            HashMap<Long, Set<Author>> authors = request.getSession().getAttribute("authorsByBook") != null ?
                    (HashMap<Long, Set<Author>>) request.getSession().getAttribute("authorsByBook") : new HashMap<>();
            if (authors.containsKey(bookId)) {
                request.setAttribute("authors", authors.get(bookId));
            } else {
                request.setAttribute("authors", bookById.getAuthors());
                authors.put(bookId, bookById.getAuthors());
                request.getSession().setAttribute("authorsByBook", authors);
            }
            ConnectionManager.close(connection);
            return "/WEB-INF/admin/adminEditBookAuthors.jsp";
        }

        String additionalAuthorName = request.getParameter("additionalAuthorName");
        String additionalAuthorSurname = request.getParameter("additionalAuthorSurname");
        String authorToDelete = request.getParameter("authorToDelete");
        String operation = request.getParameter("operation");

        HashMap<Long, Set<Author>> authorsByBook = request.getSession().getAttribute(
                "authorsByBook") != null ? (HashMap<Long, Set<Author>>) request.getSession().getAttribute(
                "authorsByBook") : new HashMap<>();
        switch (operation) {
            case "+": {
                Author additionalAuthor = new Author(additionalAuthorName, additionalAuthorSurname);
                if (checkIfAuthorIsNotValid(additionalAuthor, request, connection)) {
                    ConnectionManager.close(connection);
                    return "/WEB-INF/admin/adminEditBookAuthors.jsp";
                }
                additionalAuthor = bookService.getAuthor(additionalAuthor, connection);
                System.out.println(additionalAuthor);
                Set<Author> authors = authorsByBook.get(bookId) != null ? authorsByBook.get(bookId) : new HashSet<>();
                authors.add(additionalAuthor);
                authorsByBook.put(bookId, authors);
                request.getSession().setAttribute("authorsByBook", authorsByBook);
                ConnectionManager.close(connection);
                break;
            }
            case "-": {
                if (authorToDelete == null || !authorToDelete.matches(AUTHOR_NAME_AND_SURNAME)) {
                    ConnectionManager.close(connection);
                    return "/WEB-INF/admin/adminEditBookAuthors.jsp";
                }
                String[] split = authorToDelete.split("\\s");
                Author deleteAuthor = new Author(split[0], split[1]);
                if (checkIfAuthorIsNotValid(deleteAuthor, request, connection)) {
                    ConnectionManager.close(connection);
                    return "/WEB-INF/admin/adminEditBookAuthors.jsp";
                }
                deleteAuthor = bookService.getAuthor(deleteAuthor, connection);

                Set<Author> authors = authorsByBook.get(bookId) != null ? authorsByBook.get(bookId) : new HashSet<>();
                authors.remove(deleteAuthor);
                authorsByBook.put(bookId, authors);
                request.getSession().setAttribute("authorsByBook", authorsByBook);
                ConnectionManager.close(connection);
                break;
            }
            case "finish": {
                Book book = bookService.findBookById(bookId, connection);
                Set<Author> authors = authorsByBook.get(bookId) != null ? authorsByBook.get(bookId) : new HashSet<>();
                bookService.updateBookAuthors(bookId, authors, connection);
                authorsByBook.remove(bookId);

                book.setAuthors(authors);
                System.out.println(book);
                ConnectionManager.close(connection);
                return "redirect:/admin/books";
            }
        }
        try {
            System.out.println(connection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/manageBook?action=updateAuthors&bookId=" + bookId;
    }

    private String deleteBook(Long bookId, Connection connection) {
        Book book = bookService.findBookById(bookId, connection);
        bookService.deleteBook(book, connection);
        return "redirect:/admin/books";
    }

    private Book buildBookWithReceivedParameters(HttpServletRequest request, Connection connection) {
        String name = request.getParameter("name");
        String onlyForReadingHall = request.getParameter("onlyForReadingHall");
        Publisher publisher = new Publisher(request.getParameter("publisher.name"));
        String publishedAt = request.getParameter("publishedAt");
        String quantity = request.getParameter("quantity");
        String imgUrl = request.getParameter("imgUrl");
        Author mainAuthor = new Author(request.getParameter("mainAuthor.name"),
                request.getParameter("mainAuthor.surname"));
        boolean error = validateFields(name, publisher, mainAuthor, publishedAt, quantity, imgUrl, request, connection);
        if (error) {
            return null;
        }
        return new Book.Builder()
                .name(name)
                .publisher(bookService.getPublisher(publisher, connection))
                .onlyForReadingHall(onlyForReadingHall != null)
                .available(true)
                .publishedAt(LocalDate.parse(publishedAt))
                .quantity(Integer.parseInt(quantity))
                .imgUrl(imgUrl)
                .mainAuthor(bookService.getAuthor(mainAuthor, connection))
                .amountOfBooksTaken(0)
                .build();
    }

    private boolean validateFields(String name, Publisher publisher, Author mainAuthor,
                                   String publishedAt, String quantity, String imgUrl, HttpServletRequest request,
                                   Connection connection) {
        boolean error = false;
        if (checkIfNameIsNotValid(name, request)) {
            error = true;
        }
        if (checkIfPublisherIsNotValid(publisher, request, connection)) {
            error = true;
        }
        if (checkIfAuthorIsNotValid(mainAuthor, request, connection)) {
            error = true;
        }
        if (checkIfPublishedAtIsNotValid(publishedAt, request)) {
            error = true;
        }
        if (checkIfQuantityIsNotValid(quantity, request)) {
            error = true;
        }
        if (checkIfImageUrlIsNotValid(imgUrl, request)) {
            error = true;
        }
        return error;
    }

    private boolean checkIfNameIsNotValid(String name, HttpServletRequest request) {
        if (name != null && name.matches(NOT_EMPTY)) {
            return false;
        }
        request.setAttribute("errorName", "Name is not valid");
        return true;
    }

    private boolean checkIfPublisherIsNotValid(Publisher publisher, HttpServletRequest request, Connection connection) {
        if (bookService.checkIfPublisherExists(publisher, connection)) {
            return false;
        }
        request.setAttribute("errorPublisher", "Publisher is not valid");
        return true;
    }

    private boolean checkIfAuthorIsNotValid(Author author, HttpServletRequest request, Connection connection) {
        if (bookService.checkIfAuthorExists(author, connection)) {
            return false;
        }
        request.setAttribute("errorAuthor", "Author is not valid");
        return true;
    }

    private boolean checkIfPublishedAtIsNotValid(String publishedAt, HttpServletRequest request) {
        try {
            LocalDate localDate = LocalDate.parse(publishedAt);
            return false;
        } catch (DateTimeParseException exception) {
            request.setAttribute("errorDate", "Date is not valid");
            return true;
        }
    }

    private boolean checkIfQuantityIsNotValid(String quantity, HttpServletRequest request) {
        try {
            Integer number = Integer.parseInt(quantity);
            return false;
        } catch (NumberFormatException exception) {
            request.setAttribute("errorQuantity", "Quantity is not valid");
            return true;
        }
    }

    private boolean checkIfImageUrlIsNotValid(String imgURL, HttpServletRequest request) {
        if (imgURL != null && imgURL.matches(HAS_NO_SPACE_ELEMENT)) {
            return false;
        }
        request.setAttribute("errorImage", "Image URL is not valid");
        return true;
    }
}
