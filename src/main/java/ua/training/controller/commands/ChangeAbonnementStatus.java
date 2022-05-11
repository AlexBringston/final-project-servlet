package ua.training.controller.commands;

import ua.training.model.dao.impl.ConnectionManager;
import ua.training.model.services.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Command used to change abonnement entry status
 */
public class ChangeAbonnementStatus implements Command{

    private final OrderService orderService;

    public ChangeAbonnementStatus(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest request) throws SQLException {
        String action = request.getParameter("action");
        Long bookId = request.getParameter("bookId") != null ? Long.parseLong(request.getParameter("bookId")) : null;
        Long userId = request.getParameter("userId") != null ? Long.parseLong(request.getParameter("userId")) : null;
        if (action == null || bookId == null || userId == null) {
            throw new RuntimeException("Invalid parameters");
        }
        Connection connection = ConnectionManager.getConnection();
        try {
            orderService.changeOrderStatus(action, userId, bookId, connection);
            ConnectionManager.close(connection);
        } catch (Exception exception) {
            ConnectionManager.close(connection);
            throw new RuntimeException(exception.getMessage());
        }
        request.setAttribute("bookId", bookId);
        return "redirect:/librarian/abonnement/" + userId;
    }
}
