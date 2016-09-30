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
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/ajax/order/get/last"})
public class AjaxGetLastOrderInfoServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(AjaxGetLastOrderInfoServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));

            Order order = userService.getLastOrderInfo(accessToken);
            User passenger = userService.findById(order.getIdPassenger());

            HttpSession session = req.getSession(true);
            session.setAttribute("order", order);
            session.setAttribute("passenger", passenger);

            if (order.getIdDriver() > 0) {
                User driver = userService.findById(order.getIdDriver());
                session.setAttribute("driver", driver);
            }

            resp.getWriter().write("id:" + order.getId());

        } catch (UserNotFoundException e) {
            LOG.error(e);
            resp.getWriter().write("User not found");

        } catch (OrderNotFoundException e) {
            LOG.error(e);
            resp.getWriter().write("User doesn't have any orders");
        }
    }
}
