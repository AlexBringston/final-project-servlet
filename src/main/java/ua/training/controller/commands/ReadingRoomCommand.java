package ua.training.controller.commands;

import ua.training.model.dao.impl.ConnectionManager;
import ua.training.model.dto.Page;
import ua.training.model.entities.ReadingRoom;
import ua.training.model.services.OrderService;
import ua.training.model.utils.Constants;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;

public class ReadingRoomCommand implements Command {

    private final OrderService orderService;

    public ReadingRoomCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest request) throws SQLException {
        String action = request.getParameter("action") != null ? request.getParameter("action") : "";
        String path = "";
        switch (action) {
            case "returnOne":
                path = returnBookFromReadingRoom(request);
                break;
            case "returnAll":
                path = returnAllBooksFromReadingRoom();
                break;
            default:
                path = getReadingRoomPage(request);
                break;
        }
        return path;
    }

    private String getReadingRoomPage(HttpServletRequest request) {
        Integer page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 0;
        String sortField = (request.getParameter("sortField") != null) ? request.getParameter("sortField") :
                "room.id";
        String sortDirection = (request.getParameter("sortDirection") != null) ?
                request.getParameter("sortDirection") : "DESC";
        Connection connection = ConnectionManager.getConnection();
        try {
            Page<ReadingRoom> booksHandedOverToReadingRoom = orderService.getBooksHandedOverToReadingRoom(Constants.NUMBER_OF_ITEMS_PER_PAGE, page,
                    sortField, sortDirection, connection);
            request.setAttribute("readingRoomPage", booksHandedOverToReadingRoom);
            ConnectionManager.close(connection);
        } catch (Exception exception) {
            ConnectionManager.close(connection);
            throw new RuntimeException(exception.getMessage(), exception.getCause());
        }
        return "/WEB-INF/librarian/librarianReadingRoom.jsp";
    }

    private String returnBookFromReadingRoom(HttpServletRequest request) {
        Connection connection = ConnectionManager.getConnection();
        Long userId = request.getParameter("userId") != null ? Long.parseLong(request.getParameter("userId")) : -1;
        Long bookId = request.getParameter("bookId") != null ? Long.parseLong(request.getParameter("bookId")) : -1;
        try {
            orderService.removeTakenBookFromReadingRoom(userId, bookId, connection);
            ConnectionManager.close(connection);
        } catch (RuntimeException exception) {
            ConnectionManager.close(connection);
            throw new RuntimeException(exception.getMessage(), exception.getCause());
        }
        return "redirect:/librarian/readingRoom";
    }

    private String returnAllBooksFromReadingRoom() {
        Connection connection = ConnectionManager.getConnection();
        try {
            orderService.removeAllBooksFromReadingRoom(connection);
            ConnectionManager.close(connection);
        } catch (RuntimeException exception) {
            ConnectionManager.close(connection);
            throw new RuntimeException(exception.getMessage(), exception.getCause());
        }
        return "redirect:/librarian/readingRoom";
    }
}
