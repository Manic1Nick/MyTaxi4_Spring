package ua.artcode.taxi.servlets.orderServlets;

import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.OrderStatus;
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

@WebServlet(urlPatterns = {"/ajax/order/get/all-new"})
public class AjaxGetAllNewOrdersServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Order[] orders = (Order[]) req.getSession().getAttribute("orders");

        req.setAttribute("orders", orders);
        req.setAttribute("quantity", orders.length);

        req.getRequestDispatcher("/WEB-INF/pages/ajax-order-find-all-new.jsp").include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                                            throws ServletException, IOException {

        try {
            User found = userService.getUser(String.valueOf(req.getAttribute("accessToken")));

            Order[] orders = userService.createArrayOrdersForDriver(OrderStatus.NEW, found);

            if (orders.length > 0) {
                HttpSession session = req.getSession(true);
                session.setAttribute("orders", orders);
                resp.getWriter().print("SUCCESS");

            } else {
                resp.getWriter().print("NOTHING");
            }

        } catch (InputDataWrongException e) {
            resp.getWriter().print(e.getMessage());
        }
    }
}
