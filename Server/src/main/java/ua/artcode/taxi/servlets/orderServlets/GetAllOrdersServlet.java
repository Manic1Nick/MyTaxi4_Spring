package ua.artcode.taxi.servlets.orderServlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.model.Constants;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.OrderStatus;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.utils.BeansFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@WebServlet(urlPatterns = {"/order/all"})
public class GetAllOrdersServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(GetAllOrdersServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Map<Integer, Order> distanceMap = userService.getMapDistancesToDriver(
                    OrderStatus.NEW.toString(),
                    Constants.DRIVER_LOCATION_PATH);

            Object[] objArray = distanceMap.keySet().toArray();
            int[] distances = new int[objArray.length];
            for (int i = 0; i < objArray.length; i++) {
                distances[i] = (int) objArray[i];
            }
            Arrays.sort(distances);

            Order[] orders = new Order[distances.length];
            int[] distancesKm = new int[distances.length];

            /*String[] ids = new String[distances.length];
            String[] textOrders = new String[distances.length];*/
            for (int i = 0; i < distanceMap.size(); i++) {
                /*Order order = distanceMap.get(distances[i]);
                textOrders[i] = order.toStringForViewShort() + ", distance to you: "  +
                        distances[i]/1000 + "km";
                ids[i] = String.valueOf(order.getId());

                //add each link to each order for "order-find.jsp" (1)
                req.setAttribute(ids[i], textOrders[i]);*/

                orders[i] = distanceMap.get(distances[i]);
                distancesKm[i] = distances[i]/1000;
            }

            //add array of all links to all orders for "order-find.jsp" (2)
            /*req.setAttribute("ids", ids);*/

            /*Order[] orders = {new Order(new Address("UA KIEV"), new Address("UA UMAN")),
                                new Order(new Address("UA UMAN"), new Address("UA KIEV"))};
            orders[0].setId(3);
            orders[1].setId(10);
            int[] distancesKm = {100, 200};*/

            req.setAttribute("orders", orders);
            req.setAttribute("distances", distancesKm);

            req.getRequestDispatcher("/WEB-INF/pages/order-find.jsp").forward(req, resp);

        } catch (InputDataWrongException e) {
            LOG.error(e);
            req.setAttribute("error", "Wrong calculation in Google API");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        }
    }

}
