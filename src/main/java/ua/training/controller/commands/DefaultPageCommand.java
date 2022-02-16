package ua.training.controller.commands;

import ua.training.model.dao.impl.ConnectionManager;
import ua.training.model.dto.Page;
import ua.training.model.entities.Book;
import ua.training.model.services.BookService;
import ua.training.model.utils.Constants;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;

public class DefaultPageCommand implements Command {

    private final BookService bookService;

    public DefaultPageCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        Integer page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 0;
        String sortField = (request.getParameter("sortField") != null) ? request.getParameter("sortField") :
                "book_name";
        String sortDirection = (request.getParameter("sortDirection") != null) ? request.getParameter("sortDirection") : "ASC";
        String query = "";
        String path = "";
        if (request.getParameter("query") != null) {
            query = request.getParameter("query");
            path = "/WEB-INF/reader/search.jsp";
        } else {
            path = "/common/index.jsp";
        }
        Connection connection = ConnectionManager.getConnection();
        try {
            Page<Book> books = bookService.findBooks(query, Constants.NUMBER_OF_ITEMS_PER_PAGE, page, sortField,
                    sortDirection, connection);
            request.setAttribute("booksPage", books);
            ConnectionManager.close(connection);
        } catch (Exception exception) {
            ConnectionManager.close(connection);
            throw new RuntimeException(exception.getMessage());
        }
        return path;
    }
}
