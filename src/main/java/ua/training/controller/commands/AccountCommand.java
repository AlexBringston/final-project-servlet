package ua.training.controller.commands;

import ua.training.model.entities.User;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public class AccountCommand implements Command{
    @Override
    public String execute(HttpServletRequest request) throws SQLException {

        User user = (User) request.getSession().getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        else if (user.getRole().getName().equals("ROLE_ADMIN")) {
            return "/WEB-INF/admin/admin.jsp";
        }
        else if (user.getRole().getName().equals("ROLE_LIBRARIAN")) {
            return "/WEB-INF/librarian/librarian.jsp";
        }
        else {
            return "/WEB-INF/reader/reader.jsp";
        }
    }
}
