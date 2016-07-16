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
import java.io.IOException;

@WebServlet(urlPatterns = {"/order/make"})
public class OrderMakeServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(OrderMakeServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/pages/order-make.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String lineFrom = req.getParameter("countryFrom") + " " +
                req.getParameter("cityFrom") + " " +
                req.getParameter("streetFrom") + " " +
                req.getParameter("houseNumFrom");
        String lineTo = req.getParameter("countryTo") + " " +
                req.getParameter("cityTo") + " " +
                req.getParameter("streetTo") + " " +
                req.getParameter("houseNumTo");
        String message = req.getParameter("message");

        if (req.getMethod().equals("order-calculate")) {
            req.setAttribute("addressFrom", lineFrom);
            req.setAttribute("addressTo", lineTo);
            req.setAttribute("message", message);

            req.getRequestDispatcher("/WEB-INF/pages/order-calculate.jsp");
        }

        try {
            String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));

            Order order = userService.makeOrder(accessToken, lineFrom, lineTo, message);
            User user = userService.getUser(accessToken);

            req.setAttribute("order", order);
            req.setAttribute("user", user);

            req.getRequestDispatcher("/WEB-INF/pages/order-info.jsp").forward(req, resp);

        } catch (InputDataWrongException e) {
            LOG.error(e);
            req.setAttribute("errorTitle", "Wrong input data addresses. Can not make order");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        } catch (UserNotFoundException e) {
            LOG.error(e);
            req.setAttribute("errorTitle", "UserNotFoundException");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        } catch (OrderMakeException e) {
            LOG.error(e);
            req.setAttribute("errorTitle", "User has orders NEW or IN_PROGRESS already");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        }

    }
}
