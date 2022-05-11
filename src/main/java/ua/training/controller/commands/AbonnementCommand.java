package ua.training.controller.commands;

import ua.training.model.dao.impl.ConnectionManager;
import ua.training.model.dto.Page;
import ua.training.model.entities.Abonnement;
import ua.training.model.entities.User;
import ua.training.model.services.OrderService;
import ua.training.model.utils.Constants;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Command to get books from abonnement either to librarian or to reader
 */
public class AbonnementCommand implements Command {

   private final OrderService orderService;

    public AbonnementCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest request) throws SQLException {
        Integer page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 0;
        String sortField = (request.getParameter("sortField") != null) ? request.getParameter("sortField") :
                "return_date";
        String sortDirection = (request.getParameter("sortDirection") != null) ?
                request.getParameter("sortDirection") : "DESC";
        String path;
        String status = "";
        Connection connection = ConnectionManager.getConnection();
        User user = (User) request.getSession().getAttribute("loggedUser");
        Long userId = user.getId();
        if (user.getRole().getName().equals("ROLE_LIBRARIAN")) {
            path = "/WEB-INF/librarian/librarianReaderAbonnement.jsp";
            status = "status.handed.over";
            if (request.getParameter("userId") != null) {
                userId = Long.parseLong(request.getParameter("userId"));
            }
        } else {
            path = "/WEB-INF/reader/readerAbonnement.jsp";
        }
        try {
            Page<Abonnement> abonnementPage =
                    orderService.findCurrentUserAbonnement(userId, status, Constants.NUMBER_OF_ITEMS_PER_PAGE, page,
                            sortField,
                            sortDirection, connection);
            request.setAttribute("abonnementPage", abonnementPage);
            ConnectionManager.close(connection);
        } catch (Exception exception) {
            ConnectionManager.close(connection);
            throw new RuntimeException(exception.getMessage());
        }

        return path;
    }
}
