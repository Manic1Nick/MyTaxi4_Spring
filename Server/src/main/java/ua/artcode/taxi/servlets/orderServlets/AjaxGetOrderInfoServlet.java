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


@WebServlet(urlPatterns = {"/ajax/order/get"})
public class AjaxGetOrderInfoServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = userService.getUser(String.valueOf(req.getAttribute("accessToken")));

        HttpSession session = req.getSession(true);
        Order order = (Order) session.getAttribute("order");

        req.setAttribute("order", order);
        req.setAttribute("user", user);

        req.getRequestDispatcher("/WEB-INF/pages/ajax-order-info.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String orderId = req.getParameter("orderID");

        try {
            Order order = userService.getOrderInfo(Integer.parseInt(orderId));
            User passenger = userService.findById(order.getIdPassenger());

            HttpSession session = req.getSession(true);
            session.setAttribute("order", order);
            session.setAttribute("passenger", passenger);

            if (order.getIdDriver() > 0) {
                User driver = userService.findById(order.getIdDriver());
                session.setAttribute("driver", driver);
            }

            resp.getWriter().print("SUCCESS");

        } catch (OrderNotFoundException e) {
            resp.getWriter().print(e.getMessage());
        }
    }
}
