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

        User user = userService.getUser(String.valueOf(req.getAttribute("accessToken")));

        HttpSession session = req.getSession();
        int quantityOrders = (int) session.getAttribute("quantity");
        int quantityOrdersOnPage = Constants.QUANTITY_ORDERS_ON_HISTORY_PAGE;
        int pageMax = (int) session.getAttribute("pageMax");
        int page = (int) session.getAttribute("page");
        int to = quantityOrders - (quantityOrdersOnPage * (pageMax - page));
        int from = page == 1 ? 0 : to - quantityOrdersOnPage;

        List<Order> orders = userService.getOrdersOfUser(user.getId(), from, to);

        req.setAttribute("orders", orders);
        req.setAttribute("ordersOnPage", quantityOrdersOnPage);
        req.setAttribute("pageMax", pageMax);
        req.setAttribute("page", page);

        LOG.info("Successful attempt to get info about " + orders.size() +
                        " orders by user ID=" + user.getId() + " for history page");

        String requestDispatcher = user.getIdentifier() == UserIdentifier.P ?
                "/WEB-INF/pages/ajax-passenger-history-pages.jsp" :
                user.getIdentifier() == UserIdentifier.D ?
                "/WEB-INF/pages/ajax-driver-history-pages.jsp" : "" ;

        req.getRequestDispatcher(requestDispatcher).include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int page = Integer.parseInt(req.getParameter("page"));

        HttpSession session = req.getSession();
        session.setAttribute("page", page);

        resp.getWriter().write("SUCCESS");
    }
}
