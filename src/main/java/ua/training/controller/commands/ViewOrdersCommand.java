package ua.training.controller.commands;

import ua.training.model.dao.impl.ConnectionManager;
import ua.training.model.dto.Page;
import ua.training.model.entities.Request;
import ua.training.model.services.OrderService;
import ua.training.model.utils.Constants;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Command used to display current pending orders to librarian
 */
public class ViewOrdersCommand implements Command {

    private final OrderService orderService;

    public ViewOrdersCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest request) throws SQLException {
        Integer page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 0;
        String sortField = (request.getParameter("sortField") != null) ? request.getParameter("sortField") :
                "id";
        String sortDirection = (request.getParameter("sortDirection") != null) ?
                request.getParameter("sortDirection") : "DESC";

        Connection connection = ConnectionManager.getConnection();
        try {
            Page<Request> requests = orderService.getAllPendingRequests(Constants.NUMBER_OF_ITEMS_PER_PAGE, page, sortField,
                    sortDirection, connection);
            request.setAttribute("requestsPage", requests);
            ConnectionManager.close(connection);
        } catch (Exception exception) {
            ConnectionManager.close(connection);
            throw new RuntimeException(exception.getMessage());
        }
        return "/WEB-INF/librarian/librarianOrders.jsp";
    }
}
