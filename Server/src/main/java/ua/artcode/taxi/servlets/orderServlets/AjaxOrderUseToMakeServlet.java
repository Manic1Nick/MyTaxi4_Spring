package ua.artcode.taxi.servlets.orderServlets;

import org.apache.log4j.Logger;
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

@WebServlet(urlPatterns = {"/ajax/order/use-to-make"})
public class AjaxOrderUseToMakeServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(AjaxOrderUseToMakeServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(true);
        Order order = (Order) session.getAttribute("order");

        req.setAttribute("order", order);

        String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));
        User user = userService.getUser(accessToken);
        LOG.info("Order ID=" + order.getId() + " was used from history page by user ID=" + user.getId());

        req.getRequestDispatcher("/WEB-INF/pages/ajax-order-make.jsp").forward(req,resp);
    }
}
