package ua.training.controller.commands;

import ua.training.model.dao.impl.ConnectionManager;
import ua.training.model.entities.Request;
import ua.training.model.services.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Command used to implement management of order entries currently pending
 */
public class OrderManagementCommand implements Command{

    private final OrderService orderService;

    public OrderManagementCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest request) throws SQLException {
        String action = request.getParameter("action") != null ? request.getParameter("action") : "";
        Long requestId = request.getParameter("action") != null ? Long.parseLong(request.getParameter("requestId")) :
                -1;

        String path = "redirect:/librarian/orders";
        Connection connection = ConnectionManager.getConnection();
        try {
            switch (action) {
                case "abonnement":
                    Request requestById = orderService.findRequestById(requestId, connection);
                    request.setAttribute("request", requestById);
                    path = "/WEB-INF/librarian/librarianOrdersAbonnement.jsp";
                    break;
                case "readingHall":
                    orderService.addRequestToReadingRoom(requestId, connection);
                    break;
                case "reject":
                    orderService.rejectRequest(requestId, connection);
                    break;

            }
            ConnectionManager.close(connection);
        } catch (Exception exception) {
            ConnectionManager.close(connection);
            throw new RuntimeException(exception.getMessage());
        }
        return path;
    }
}
