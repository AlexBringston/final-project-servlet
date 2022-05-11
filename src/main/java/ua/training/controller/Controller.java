package ua.training.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.controller.commands.*;
import ua.training.model.services.BookService;
import ua.training.model.services.OrderService;
import ua.training.model.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Main controller used to manage all requests and redirect them to corresponding commands
 */
public class Controller extends HttpServlet {

    /**
     * Map of all commands available in system
     */
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Logger instance
     */
    private final Logger logger = LogManager.getLogger(Controller.class);

    /**
     * Init method to initialize map with commands and their mappings
     */
    @Override
    public void init(){
        BookService bookService = new BookService();
        UserService userService = new UserService();
        OrderService orderService = new OrderService();

        // general commands
        commands.put("index", new DefaultPageCommand(bookService));
        commands.put("login", new LoginCommand(userService));
        commands.put("logout", new LogoutCommand());
        commands.put("registration", new RegistrationCommand(userService));
        commands.put("account", new AccountCommand());

        // reader commands
        commands.put("reader/abonnement", new AbonnementCommand(orderService));
        commands.put("reader/createOrder", new CreateOrderCommand(orderService));
        commands.put("reader/search", new DefaultPageCommand(bookService));

        // librarian commands
        commands.put("librarian/orders", new ViewOrdersCommand(orderService));
        commands.put("librarian/readers", new ViewReadersCommand(userService));
        commands.put("librarian/abonnement", new AbonnementCommand(orderService));
        commands.put("librarian/changeAbonnementStatus", new ChangeAbonnementStatus(orderService));
        commands.put("librarian/orderManagement", new OrderManagementCommand(orderService));
        commands.put("librarian/addOrder", new AddOrderToAbonnementCommand(orderService));
        commands.put("librarian/readingRoom", new ReadingRoomCommand(orderService));

        // admin commands
        commands.put("admin/books", new BookCommand(bookService));
        commands.put("admin/librarians", new ManageLibrariansCommand(userService));
        commands.put("admin/users", new ViewUsersCommand(userService));
        commands.put("admin/addLibrarian", new AddLibrarianCommand(userService));
        commands.put("admin/manageBook", new ManageBookCommand(bookService));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }


    private void processRequest(HttpServletRequest request,
                                HttpServletResponse response) throws IOException, ServletException {
        String path = request.getRequestURI();
        path = path.replaceAll(".*/app/", "");
        Command command = commands.getOrDefault(path, (r) -> "redirect:/index");
        try {
            String page = command.execute(request);
            if (page.contains("redirect:")) {
                response.sendRedirect(page.replace("redirect:", "/app"));
            } else {
                request.getRequestDispatcher(page).forward(request, response);
            }
        } catch (Exception e) {
            logger.log(Level.WARN, e.getMessage());
            request.setAttribute("errorMessage",e.getMessage());
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request,response);
        }
    }
}
