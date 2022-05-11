package ua.training.controller.commands;

import ua.training.model.dao.impl.ConnectionManager;
import ua.training.model.entities.User;
import ua.training.model.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.LocalDate;

import static ua.training.model.utils.RegEx.*;
import static ua.training.model.utils.RegEx.PASSWORD_REGEX;

/**
 * Command used to implement registration mechanism
 */
public class RegistrationCommand implements Command{

    private final UserService userService;

    public RegistrationCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) throws SQLException {
        if (request.getSession().getAttribute("loggedUser") != null) {
            return "redirect:/account";
        }
        if (request.getMethod().equals("GET")) {
            return "/common/registration.jsp";
        }
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        LocalDate birthDate = LocalDate.parse(request.getParameter("birthDate"));
        User user = null;
        if ((name != null && name.matches(NAME_REGEX) && (surname != null && surname.matches(SURNAME_REGEX))
                && (username != null && username.matches(USERNAME_REGEX)) && (password != null && password.matches(PASSWORD_REGEX))
                && birthDate.isBefore(LocalDate.now())) ) {
            user = new User(name, surname, username, password, birthDate);
        }
        else {
            request.setAttribute("","");
            return "/common/registration.jsp";
        }
        userService.registerNewUser(user,"ROLE_READER", ConnectionManager.getConnection());
        request.setAttribute("message", "Your account was successfully created, please proceed to the login page");
        return "/WEB-INF/message.jsp";
    }
}
