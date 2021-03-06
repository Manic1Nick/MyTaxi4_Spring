package ua.artcode.taxi.servlets.userServlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.model.Constants;
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

@WebServlet(urlPatterns = {"/ajax/user/get/orders"})
public class AjaxGetAllUserOrdersServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(AjaxGetAllUserOrdersServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                                            throws ServletException, IOException {

        User found = userService.getUser(String.valueOf(req.getAttribute("accessToken")));
        int quantityOrders = userService.getQuantityOrdersOfUser(found.getId());

        if (quantityOrders > 0) {
            HttpSession session = req.getSession(true);
            session.setAttribute("quantity", quantityOrders);

            int ordersOnPage = Constants.QUANTITY_ORDERS_ON_HISTORY_PAGE;
            int pageMax = quantityOrders % ordersOnPage == 0 ?
                    quantityOrders / ordersOnPage : (quantityOrders / ordersOnPage) + 1;
            session.setAttribute("page", pageMax);
            session.setAttribute("pageMax", pageMax);

            LOG.info("Get all orders to history page by user ID=" + found.getId());

            resp.getWriter().print("SUCCESS");

        } else {

            LOG.info("Not found orders to history page for user ID=" + found.getId());
            resp.getWriter().print("NOTHING");
        }
    }
}
