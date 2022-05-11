package ua.training.controller.commands;

import ua.training.model.dao.impl.ConnectionManager;
import ua.training.model.entities.User;
import ua.training.model.services.UserService;

import javax.servlet.http.HttpServletRequest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static ua.training.model.utils.RegEx.USERNAME_REGEX;

/**
 * Command used to implement login on site
 */
public class LoginCommand implements Command {

    private final UserService userService;

    public LoginCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) throws SQLException {
        if (request.getSession().getAttribute("loggedUser") != null) {
            return "redirect:/account";
        }
        if (request.getMethod().equals("GET")) {
            return "/common/login.jsp";
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (!username.matches(USERNAME_REGEX) || password == null || password.equals("")) {
            request.setAttribute("errorMessage", "error.data.entered");
            return "/common/login.jsp";
        }

        Connection connection = ConnectionManager.getConnection();
        User user = userService.findUserByUsername(username, connection);
        if (!user.getPassword().equals(password)) {
            request.setAttribute("errorMessage", "error.data.entered");
            return "/common/login.jsp";
        }
        Set<String> loggedUsers = (Set<String>) request.getServletContext().getAttribute("loggedUsers");
        if (loggedUsers == null) {
            loggedUsers = new HashSet<>();
            loggedUsers.add(username);
            request.getSession().getServletContext().setAttribute("loggedUsers", loggedUsers);
        }
        else if (loggedUsers.contains(user.getUsername())) {
            request.setAttribute("errorMessage", "error.user.logged");
            return "/common/login.jsp";
        } else {
            loggedUsers.add(user.getUsername());
            request.getSession().getServletContext().setAttribute("loggedUsers", loggedUsers);
        }
        request.getSession().setAttribute("loggedUser", user);
        return "redirect:/account";
    }
}
