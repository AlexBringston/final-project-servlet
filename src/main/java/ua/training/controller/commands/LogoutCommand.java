package ua.training.controller.commands;

import ua.training.model.entities.User;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class LogoutCommand implements Command{
    @Override
    public String execute(HttpServletRequest request) throws SQLException {
        Set<String> loggedUsers = (HashSet<String>) request.getServletContext().getAttribute("loggedUsers");
        String loggedUser = ((User)request.getSession(false).getAttribute("loggedUser")).getUsername();
        loggedUsers.remove(loggedUser);
        request.getServletContext().setAttribute("loggedUsers", loggedUsers);
        request.getSession(false).invalidate();
        return "redirect:/login";
    }
}
