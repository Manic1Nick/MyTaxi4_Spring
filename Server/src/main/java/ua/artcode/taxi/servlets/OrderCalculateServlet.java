package ua.artcode.taxi.servlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.model.Address;
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

        Address addressFrom = new Address(req.getParameter("countryFrom") + " " +
                req.getParameter("cityFrom") + " " +
                req.getParameter("streetFrom") + " " +
                req.getParameter("houseNumFrom"));
        Address addressTo = new Address(req.getParameter("countryTo") + " " +
                req.getParameter("cityTo") + " " +
                req.getParameter("streetTo") + " " +
                req.getParameter("houseNumTo"));

        req.setAttribute("addressFrom", addressFrom);
        req.setAttribute("addressTo", addressTo);
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

        try {
            Map<String, Object> map = userService.calculateOrder(lineFrom, lineTo);

            req.setAttribute("distance", map.get("distance").toString());
            req.setAttribute("price", map.get("price").toString());
            req.getRequestDispatcher("/WEB-INF/pages/order-make.jsp").forward(req, resp);

        } catch (Exception e) {
            LOG.error(e);
            req.setAttribute("error", e);
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        }

    }
}
