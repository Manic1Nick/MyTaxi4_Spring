package ua.artcode.taxi.servlets.userServlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.model.Constants;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.model.UserIdentifier;
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

@WebServlet(urlPatterns = {"/ajax/user-history"})
public class AjaxUserHistoryServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(AjaxUserHistoryServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        String accessToken = String.valueOf(session.getAttribute("accessToken"));
        User user = userService.getUser(accessToken);

        int quantityOrders = (int) session.getAttribute("quantity");
        int quantityOrdersOnPage = Constants.quantityOrdersOnPageForUserHistory;
        int pageMax = (int) session.getAttribute("pageMax");
        int page = (int) session.getAttribute("page");
        int to = quantityOrders - (quantityOrdersOnPage * (pageMax - page));
        int from = page == 1 ? 0 : to - quantityOrdersOnPage;

        List<Order> orders = userService.getOrdersOfUser(user.getId(), from, to);

        req.setAttribute("orders", orders);
        req.setAttribute("ordersOnPage", quantityOrdersOnPage);
        req.setAttribute("pageMax", pageMax);
        req.setAttribute("page", page);

        UserIdentifier identifier = userService.getUser(accessToken).getIdentifier();
        if (identifier.equals(UserIdentifier.P)) {
            req.getRequestDispatcher("/WEB-INF/pages/ajax-passenger-history-pages.jsp").include(req, resp);

        } else if (identifier.equals(UserIdentifier.D)) {
            req.getRequestDispatcher("/WEB-INF/pages/ajax-driver-history-pages.jsp").include(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int page = Integer.parseInt(req.getParameter("page"));

        HttpSession session = req.getSession();
        session.setAttribute("page", page);

        resp.getWriter().write("SUCCESS");
    }
}
