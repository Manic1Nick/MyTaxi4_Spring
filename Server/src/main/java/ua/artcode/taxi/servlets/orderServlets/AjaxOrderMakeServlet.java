package ua.artcode.taxi.servlets.orderServlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.exception.OrderMakeException;
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

@WebServlet(urlPatterns = {"/ajax/order/make"})
public class AjaxOrderMakeServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(AjaxOrderMakeServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        session.setAttribute("order", null);

        req.getRequestDispatcher("/WEB-INF/pages/ajax-order-make.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String lineFrom = req.getParameter("addressFrom");
        String lineTo = req.getParameter("addressTo");
        String message = req.getParameter("message");

        try {
            String accessToken = String.valueOf(req.getAttribute("accessToken"));

            Order order = userService.makeOrder(accessToken, lineFrom, lineTo, message);

            HttpSession session = req.getSession(true);
            session.setAttribute("order", order);

            User user = userService.getUser(accessToken);
            LOG.info("New order ID=" + order.getId() + " was created by user ID=" + user.getId());

            resp.getWriter().print("orderID:" + order.getId());

        } catch (InputDataWrongException | OrderMakeException | UserNotFoundException e) {
            LOG.error(e);
            resp.getWriter().print(e.getMessage());
        }
    }
}
