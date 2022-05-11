package ua.training.controller.commands;

import ua.training.model.dao.impl.ConnectionManager;
import ua.training.model.dto.Page;
import ua.training.model.entities.User;
import ua.training.model.services.UserService;
import ua.training.model.utils.Constants;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Command used to display readers in system for librarian to use
 */
public class ViewReadersCommand implements Command{

    private final UserService userService;

    public ViewReadersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) throws SQLException {
        Integer page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 0;
        String sortField = (request.getParameter("sortField") != null) ? request.getParameter("sortField") :
                "surname";
        String sortDirection = (request.getParameter("sortDirection") != null) ?
                request.getParameter("sortDirection") : "ASC";

        Connection connection = ConnectionManager.getConnection();
        try {
            Page<User> userPage = userService.getUsersByRolePerPage("ROLE_READER",Constants.NUMBER_OF_ITEMS_PER_PAGE,
                    page, sortField, sortDirection, connection);
            request.setAttribute("readersPage", userPage);
            ConnectionManager.close(connection);
        } catch (Exception exception) {
            ConnectionManager.close(connection);
            throw new RuntimeException(exception.getMessage());
        }
        return "/WEB-INF/librarian/librarianReaders.jsp";
    }
}