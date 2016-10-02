package ua.artcode.taxi.servlets.orderServlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.exception.DriverOrderActionException;
import ua.artcode.taxi.exception.OrderNotFoundException;
import ua.artcode.taxi.exception.WrongStatusOrderException;
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

@WebServlet(urlPatterns = {"/ajax/order/take"})
public class AjaxOrderTakeServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(AjaxOrderTakeServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String orderId = req.getParameter("orderID");

        try {
            String accessToken = String.valueOf(req.getAttribute("accessToken"));

            Order order = userService.takeOrder(accessToken, Long.parseLong(orderId));

            HttpSession session = req.getSession(true);
            session.setAttribute("order", order);

            User user = userService.getUser(accessToken);
            LOG.info("Order ID=" + order.getId() + " was taken by user ID=" + user.getId());

            resp.getWriter().print("TAKEN");

        } catch (DriverOrderActionException | WrongStatusOrderException | OrderNotFoundException e) {
            LOG.error(e);
            resp.getWriter().print(e.getMessage());
        }
    }

}
