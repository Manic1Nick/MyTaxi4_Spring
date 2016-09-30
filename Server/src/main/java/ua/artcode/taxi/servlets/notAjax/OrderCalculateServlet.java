package ua.artcode.taxi.servlets.notAjax;

import org.apache.log4j.Logger;
import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.model.Address;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.utils.BeansFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns = {"/order-calculate"})
public class OrderCalculateServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(OrderCalculateServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String lineFrom = req.getParameter("countryFrom") + " " +
                req.getParameter("cityFrom") + " " +
                req.getParameter("streetFrom") + " " +
                req.getParameter("houseNumFrom");
        String lineTo = req.getParameter("countryTo") + " " +
                req.getParameter("cityTo") + " " +
                req.getParameter("streetTo") + " " +
                req.getParameter("houseNumTo");

        try {
            Map<String, Object> map = userService.calculateOrder(lineFrom, lineTo);

            Order testOrder = new Order(new Address(lineFrom), new Address(lineTo));
            testOrder.setDistance((int) map.get("distance"));
            testOrder.setDistance((int) map.get("price"));

            req.setAttribute("testOrder", testOrder);

            req.getRequestDispatcher("/WEB-INF/pages/order-make.jsp").forward(req,resp);

        } catch (InputDataWrongException e) {
            LOG.error(e);
            req.setAttribute("errorTitle", "Wrong input data. Can not make order");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        }
    }
}
