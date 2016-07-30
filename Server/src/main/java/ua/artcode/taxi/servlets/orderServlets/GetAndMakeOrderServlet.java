package ua.artcode.taxi.servlets.orderServlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.exception.OrderNotFoundException;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.utils.BeansFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(urlPatterns = {"/ajax/order/get-and-make"})
public class GetAndMakeOrderServlet extends HttpServlet {


    private UserService userService;
    private static final Logger LOG = Logger.getLogger(GetAndMakeOrderServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String orderId = req.getParameter("id");

        try {
            Order order = userService.getOrderInfo(Integer.parseInt(orderId));

            req.setAttribute("order", order);

            req.getRequestDispatcher("/WEB-INF/pages/ajax-order-make.jsp").forward(req, resp);

        } catch (OrderNotFoundException e) {
            LOG.error(e);
            resp.getWriter().print("Order not found");
        }
    }
}
