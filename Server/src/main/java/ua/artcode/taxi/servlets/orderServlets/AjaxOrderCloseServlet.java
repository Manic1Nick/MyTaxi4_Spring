package ua.artcode.taxi.servlets.orderServlets;

import ua.artcode.taxi.exception.DriverOrderActionException;
import ua.artcode.taxi.exception.OrderNotFoundException;
import ua.artcode.taxi.exception.WrongStatusOrderException;
import ua.artcode.taxi.model.Order;
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

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String orderId = req.getParameter("orderID");

        try {
            String accessToken = String.valueOf(req.getAttribute("accessToken"));

            Order order = userService.closeOrder(accessToken, Integer.parseInt(orderId));

            HttpSession session = req.getSession(true);
            session.setAttribute("order", order);
            resp.getWriter().print("CLOSED");

        } catch (OrderNotFoundException | WrongStatusOrderException | DriverOrderActionException e) {
            resp.getWriter().print(e.getMessage());
        }
    }
}
