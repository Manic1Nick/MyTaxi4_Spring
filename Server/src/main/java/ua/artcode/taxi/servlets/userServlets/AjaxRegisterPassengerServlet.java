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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/ajax/register-passenger"})
public class AjaxRegisterPassengerServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(AjaxRegisterPassengerServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Object accessTokenObj = req.getSession().getAttribute("accessToken");

        if (accessTokenObj != null) {
            String accessToken = String.valueOf(accessTokenObj);
            User user = userService.getUser(accessToken);
            req.setAttribute("user", user);
        }

        req.getRequestDispatcher("/WEB-INF/pages/ajax-register-passenger.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<String, String> registerData = new HashMap<>();

        registerData.put("phone", req.getParameter("phone"));
        registerData.put("pass", req.getParameter("password"));
        registerData.put("name", req.getParameter("name"));
        registerData.put("homeAddress",
                req.getParameter("country") + " "
                + req.getParameter("city") + " "
                + req.getParameter("street") + " "
                + req.getParameter("houseNum"));

        try {
            String accessToken = String.valueOf(req.getAttribute("accessToken"));

            if (accessToken != null) {
                User user = userService.updateUser(registerData, accessToken);

                LOG.info("Successful attempt to change register data by user ID=" + user.getId());

            } else {
                User user = userService.registerPassenger(registerData);
                accessToken = userService.login(user.getPhone(), user.getPass());

                HttpSession session = req.getSession(true);
                session.setAttribute("inSystem", true);
                session.setAttribute("accessToken", accessToken);
                session.setAttribute("currentUserID", user.getId());

                LOG.info("Successful attempt to register new passenger ID=" + user.getId());
            }

            resp.getWriter().write("SUCCESS");

        } catch (Exception e) {
            LOG.error(e);
            resp.getWriter().write(e.getMessage());
        }
    }
}


