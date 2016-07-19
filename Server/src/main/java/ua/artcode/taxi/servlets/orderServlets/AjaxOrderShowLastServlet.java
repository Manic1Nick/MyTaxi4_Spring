package ua.artcode.taxi.servlets.orderServlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.exception.OrderNotFoundException;
import ua.artcode.taxi.exception.UserNotFoundException;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.utils.BeansFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/ajax/order/last"})
public class AjaxOrderShowLastServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(AjaxOrderShowLastServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));

            Order order = userService.getLastOrderInfo(accessToken);
            User user = userService.getUser(accessToken);

            req.setAttribute("order", order);
            req.setAttribute("user", user);

            req.getRequestDispatcher("/WEB-INF/pages/ajax-order-info.jsp").include(req, resp);

        } catch (UserNotFoundException e) {
            LOG.error(e);
            resp.getWriter().write("User not found");

        } catch (OrderNotFoundException e) {
            LOG.error(e);
            resp.getWriter().write("User doesn't have any orders");
        }
    }
}
