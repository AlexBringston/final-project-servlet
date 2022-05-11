package ua.training.controller.commands;

import ua.training.model.dao.impl.ConnectionManager;
import ua.training.model.entities.User;
import ua.training.model.services.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;

public class CreateOrderCommand implements Command{

    private final OrderService orderService;

    public CreateOrderCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest request) throws SQLException {
        Long bookId = (request.getParameter("bookId") != null) ? Long.parseLong(request.getParameter("bookId")) : -1;
        if (bookId == -1) {
            request.setAttribute("errorMessage", "error.invalid.id");
            return "/WEB-INF/error.jsp";
        }
        Connection connection = ConnectionManager.getConnection();
        try {
            User user = (User) request.getSession().getAttribute("loggedUser");
            orderService.createOrderOnGivenBook(user.getId(), bookId, connection);
            ConnectionManager.close(connection);
        } catch (Exception exception) {
            ConnectionManager.close(connection);
            throw new RuntimeException("error.create.order");
        }
        request.setAttribute("message", "Your order was successfully created, " +
                "please wait for the librarian to confirm it");
        return "/WEB-INF/message.jsp";
    }
}
