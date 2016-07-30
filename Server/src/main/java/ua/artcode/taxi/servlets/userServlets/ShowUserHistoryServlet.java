package ua.artcode.taxi.servlets.userServlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.model.Constants;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.utils.BeansFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

        HttpSession session = req.getSession();
        String accessToken = String.valueOf(session.getAttribute("accessToken"));
        User user = userService.getUser(accessToken);
        int page = (int) session.getAttribute("page");

        if (req.getParameter("move") != null) {
            int move = Integer.parseInt(req.getParameter("move"));
            page = page + move;
        }
        if (page > 0) {
            int quantityOrders = (int) session.getAttribute("quantity");
            int from = 0;
            int to = 0;

            if (quantityOrders < 11) {
                to = quantityOrders;

            } else {
                to = page * Constants.quantityOrdersOnPageForUserHistory;
                from = to - Constants.quantityOrdersOnPageForUserHistory;
            }

            List<Order> orders = userService.getOrdersOfUser(user, from, to);
            req.setAttribute("orders", orders);

            req.getRequestDispatcher("/WEB-INF/pages/user-history.jsp").include(req, resp);
        }
    }
}
