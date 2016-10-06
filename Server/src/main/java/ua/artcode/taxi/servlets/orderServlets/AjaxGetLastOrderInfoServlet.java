package ua.artcode.taxi.servlets.orderServlets;

import ua.artcode.taxi.exception.OrderNotFoundException;
import ua.artcode.taxi.exception.UserNotFoundException;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.utils.BeansFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/ajax/order/get/last"})
public class AjaxGetLastOrderInfoServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));
            Order order = userService.getLastOrderInfo(accessToken);
            resp.getWriter().write("id:" + order.getId());

        } catch (UserNotFoundException | OrderNotFoundException e) {
            resp.getWriter().write(e.getMessage());
        }
    }
}
