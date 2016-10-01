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

@WebServlet(urlPatterns = {"/ajax/order/close"})
public class AjaxOrderCloseServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(AjaxOrderCloseServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String orderId = req.getParameter("id");

        try {
            String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));

            Order order = userService.closeOrder(accessToken, Integer.parseInt(orderId));

            HttpSession session = req.getSession(true);
            session.setAttribute("order", order);

            User user = userService.getUser(accessToken);
            LOG.info("Order ID=" + order.getId() + " was closed by user ID=" + user.getId());

            resp.getWriter().print("CLOSED");

        } catch (OrderNotFoundException e) {
            LOG.error(e);
            resp.getWriter().print("Order not found");

        } catch (WrongStatusOrderException e) {
            LOG.error(e);
            resp.getWriter().print("Order must have status IN_PROGRESS to close");

        } catch (DriverOrderActionException e) {
            LOG.error(e);
            resp.getWriter().print("This order is not your order");
        }
    }

}
