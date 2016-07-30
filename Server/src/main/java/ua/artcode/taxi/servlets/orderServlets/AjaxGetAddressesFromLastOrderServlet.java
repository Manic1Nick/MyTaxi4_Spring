package ua.artcode.taxi.servlets.orderServlets;

import org.apache.log4j.Logger;
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

@WebServlet(urlPatterns = {"/ajax/order/last-addresses"})
public class AjaxGetAddressesFromLastOrderServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(AjaxGetAddressesFromLastOrderServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));

            Order order = userService.getLastOrderInfo(accessToken);

            String addressFrom = order.getFrom().getCountry() + "," +
                    order.getFrom().getCity() + "," +
                    order.getFrom().getStreet() + "," +
                    order.getFrom().getHouseNum();

            String addressTo = order.getTo().getCountry() + "," +
                    order.getTo().getCity() + "," +
                    order.getTo().getStreet() + "," +
                    order.getTo().getHouseNum();

            resp.getWriter().print(addressFrom + ";" + addressTo);

        } catch (UserNotFoundException e) {
            LOG.error(e);
            resp.getWriter().write("User not found");

        } catch (OrderNotFoundException e) {
            LOG.error(e);
            resp.getWriter().write("User doesn't have any orders");
        }
    }
}
