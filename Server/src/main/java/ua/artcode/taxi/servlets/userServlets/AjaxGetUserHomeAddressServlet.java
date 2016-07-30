package ua.artcode.taxi.servlets.userServlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.utils.BeansFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/ajax/user/get-address"})
public class AjaxGetUserHomeAddressServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(AjaxGetUserHomeAddressServlet.class);

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));

            User found = userService.getUser(accessToken);

            req.setAttribute("user", found);

            String homeAddress = found.getHomeAddress().getCountry() + "," +
                    found.getHomeAddress().getCity() + "," +
                    found.getHomeAddress().getStreet() + "," +
                    found.getHomeAddress().getHouseNum();

            resp.getWriter().write(homeAddress);

        } catch (Exception e) {
            LOG.error(e);
            resp.getWriter().write("User not found");
        }
    }

}
