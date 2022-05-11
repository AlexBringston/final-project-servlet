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
 * Command used to display users to admin or to manage different actions on them by him
 */
public class ViewUsersCommand implements Command{

    private final UserService userService;

    public ViewUsersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        if (request.getMethod().equals("POST")) {
            String action = (request.getParameter("action") != null) ? request.getParameter("action") : "";
            Long userId = (request.getParameter("userId") != null) ? Long.parseLong(request.getParameter("userId")) :
                    -1;
            userService.changeUserStatus(action, userId, connection);
            ConnectionManager.close(connection);
            return "redirect:/admin/users";
        } else {
            Integer page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 0;
            String sortField = (request.getParameter("sortField") != null) ? request.getParameter("sortField") :
                    "surname";
            String sortDirection = (request.getParameter("sortDirection") != null) ?
                    request.getParameter("sortDirection") : "ASC";
            try {
                Page<User> userPage = userService.getUsersByRolePerPage("ROLE_READER", Constants.NUMBER_OF_ITEMS_PER_PAGE,
                        page, sortField, sortDirection, connection);
                request.setAttribute("usersPage", userPage);
                request.setAttribute("ages", userService.findUsersAges(userPage.getObjects()));
                request.setAttribute("booksTaken", userService.countBooksInUsersAbonnements(userPage.getObjects(), connection));
                ConnectionManager.close(connection);
            } catch (Exception exception) {
                ConnectionManager.close(connection);
                throw new RuntimeException(exception.getMessage());
            }
            return "/WEB-INF/admin/adminUsers.jsp";
        }
    }
}