package ua.artcode.taxi.servlets.orderServlets;

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
import java.util.Date;


@WebServlet(urlPatterns = {"/ajax/order/add-message"})
public class AjaxAddMessageToOrderServlet extends HttpServlet {

    private UserService userService;

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
            String orderId = req.getParameter("orderID");
            String message = req.getParameter("message");

            User user = userService.getUser(String.valueOf(req.getAttribute("accessToken")));
            Order order = userService.getOrderInfo(Integer.parseInt(orderId));

            String messageRes = String.format("%s" + "\n" + "%s: %s, %s",
                    order.getMessage(), user.getName(), message, new Date().toString());
            order.setMessage(messageRes);
            Order updatedOrder = userService.updateOrder(order);

            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("order", updatedOrder);

            resp.getWriter().print("SUCCESS");

        } catch (OrderNotFoundException e) {
            resp.getWriter().print(e.getMessage());
        }
    }
}
