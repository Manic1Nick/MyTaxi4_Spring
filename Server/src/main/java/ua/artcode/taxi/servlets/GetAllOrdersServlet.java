package ua.artcode.taxi.servlets;

import org.apache.log4j.Logger;
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

// it more clear name all
@WebServlet(urlPatterns = {"/order/all"})// do it get all orders  or just one?
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

            //get id from parameter
            // show orders page
            // who do call this servlet? driver
            //driver get all orders with status NEW



            Map<Integer, Order> distanceMap = userService.getMapDistancesToDriver(
                    OrderStatus.NEW.toString(),
                    Constants.DRIVER_LOCATION_PATH);

            Object[] objArray = distanceMap.keySet().toArray();
            int[] distances = new int[objArray.length];
            for (int i = 0; i < objArray.length; i++) {
                distances[i] = (int) objArray[i];
            }
            Arrays.sort(distances);

            String[] linkOrders = new String[distances.length];
            String[] textOrders = new String[distances.length];
            for (int i = 0; i < distanceMap.size(); i++) {
                Order order = distanceMap.get(distances[i]);
                textOrders[i] = order.toStringForViewShort() + ", distance to you: "  +
                        distances[i]/1000 + "km";
                linkOrders[i] = "get?id=" + order.getId();

                //add each link to each order for "order-find.jsp" (1)
                req.setAttribute(linkOrders[i], textOrders[i]);
                // not good, wrap this values and send more structured data
            }

            //add array of all links to all orders for "order-find.jsp" (2)
            req.setAttribute("links", linkOrders);

            req.getRequestDispatcher("/WEB-INF/pages/order-find.jsp").forward(req, resp);

        } catch (Exception e) {
            LOG.error(e);
            req.setAttribute("error", e);
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        }
    }

}
