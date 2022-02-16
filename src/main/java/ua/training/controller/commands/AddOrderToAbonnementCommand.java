package ua.training.controller.commands;

import ua.training.model.dao.impl.ConnectionManager;
import ua.training.model.services.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddOrderToAbonnementCommand implements Command{

    private final OrderService orderService;

    public AddOrderToAbonnementCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest request) throws SQLException {
        Long bookId = request.getParameter("bookId") != null ? Long.parseLong(request.getParameter("bookId")) : -1;
        Long userId = request.getParameter("userId") != null ? Long.parseLong(request.getParameter("userId")) : -1;
        LocalDate date = request.getParameter("returnDate") != null ? LocalDate.parse(request.getParameter(
                "returnDate")) :
                LocalDate.now();
        Double penalty = request.getParameter("penalty") != null ?
                Double.parseDouble(request.getParameter("penalty")) : -1;
        Long requestId = request.getParameter("requestId") != null ? Long.parseLong(request.getParameter("requestId")) : -1;
        Connection connection = ConnectionManager.getConnection();
        try {
            orderService.configureNewOrderForAbonnement(bookId,userId,date,penalty,requestId,connection);
            ConnectionManager.close(connection);
        } catch (Exception exception) {
            ConnectionManager.close(connection);
            throw new RuntimeException(exception.getMessage());
        }
        return "redirect:/librarian/orders";
    }
}
