package ua.training.controller.commands;

import ua.training.model.dao.impl.ConnectionManager;
import ua.training.model.entities.User;
import ua.training.model.services.UserService;
import ua.training.model.utils.RegEx;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.LocalDate;

import static ua.training.model.utils.RegEx.*;

public class AddLibrarianCommand implements Command{

    private final UserService userService;

    public AddLibrarianCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) throws SQLException {
        if (request.getMethod().equals("GET")) {
            return "/WEB-INF/admin/adminNewLibrarian.jsp";
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
            return "/WEB-INF/admin/adminNewLibrarian.jsp";
        }
        userService.registerNewUser(user,"ROLE_LIBRARIAN", ConnectionManager.getConnection());
        return "redirect:/admin/librarians";
    }
}
