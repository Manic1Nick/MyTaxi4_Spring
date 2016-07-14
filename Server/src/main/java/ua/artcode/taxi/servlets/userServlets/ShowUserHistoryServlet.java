package ua.artcode.taxi.servlets.userServlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.utils.BeansFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/user-history"})
public class ShowUserHistoryServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(ShowUserHistoryServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));

        List<Order> orders = userService.getAllOrdersUser(accessToken);

        req.setAttribute("orders", orders);

        req.getRequestDispatcher("/WEB-INF/pages/user-history.jsp").forward(req, resp);
    }
}
