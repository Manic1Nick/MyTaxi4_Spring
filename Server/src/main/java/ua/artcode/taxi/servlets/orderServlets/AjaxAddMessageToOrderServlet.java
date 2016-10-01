package ua.artcode.taxi.servlets.orderServlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.exception.OrderNotFoundException;
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


@WebServlet(urlPatterns = {"/ajax/order/add-message"})
public class AjaxAddMessageToOrderServlet extends HttpServlet {


    private UserService userService;
    private static final Logger LOG = Logger.getLogger(AjaxAddMessageToOrderServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Order order = (Order) req.getSession().getAttribute("order");

        req.setAttribute("order", order);

        req.getRequestDispatcher("/WEB-INF/pages/ajax-order-info.jsp").include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String orderId = req.getParameter("id");
            String message = req.getParameter("message");

            String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));
            User user = userService.getUser(accessToken);

            Order order = userService.getOrderInfo(Integer.parseInt(orderId));
            String messageRes = order.getMessage() + "\n" + user.getName() + ": " + message;
            order.setMessage(messageRes);
            Order updatedOrder = userService.updateOrder(order);

            HttpSession session = req.getSession(true);
            session.setAttribute("order", updatedOrder);

            LOG.info("Message was added to order ID=" + orderId);

            resp.getWriter().print("SUCCESS");

        } catch (OrderNotFoundException e) {
            LOG.error(e);
            resp.getWriter().print("Order not found");
        }
    }
}
