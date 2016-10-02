package ua.artcode.taxi.servlets.orderServlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.exception.OrderNotFoundException;
import ua.artcode.taxi.exception.UserNotFoundException;
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
            Order order = userService.getLastOrderInfo(String.valueOf(req.getAttribute("accessToken")));

            Address from = order.getFrom();
            String addressFrom = from.getCountry() + "," +
                    from.getCity() + "," +
                    from.getStreet() + "," +
                    from.getHouseNum();

            Address to = order.getTo();
            String addressTo = to.getCountry() + "," +
                    to.getCity() + "," +
                    to.getStreet() + "," +
                    to.getHouseNum();

            LOG.info("Addresses have been obtained from order ID=" + order.getId());

            resp.getWriter().print(addressFrom + ";" + addressTo);

        } catch (UserNotFoundException | OrderNotFoundException e) {
            LOG.error(e);
            resp.getWriter().write(e.getMessage());
        }
    }
}
