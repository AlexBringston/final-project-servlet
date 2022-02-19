package ua.training.controller.filters;

import ua.training.model.entities.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ReaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        User user = (User) ((HttpServletRequest) request).getSession().getAttribute("loggedUser");
        if (user != null && user.getRole().getName().equals("ROLE_READER")) {
            chain.doFilter(request, response);
        } else {
            throw new RuntimeException("You cannot enter this page");
        }

    }
}
