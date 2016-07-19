package ua.artcode.taxi.servlets.userServlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.exception.RegisterException;
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

@WebServlet(urlPatterns = {"/ajax/register-driver"})
public class AjaxRegisterDriverServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(AjaxRegisterDriverServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Object accessTokenObj = req.getSession().getAttribute("accessToken");

        if (accessTokenObj != null) {
            User user = userService.getUser(String.valueOf(accessTokenObj));
            req.setAttribute("user", user);
        }

        req.getRequestDispatcher("/WEB-INF/pages/ajax-register-driver.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<String, String> registerData = new HashMap<>();

        registerData.put("phone", req.getParameter("phone"));
        registerData.put("pass", req.getParameter("pass"));
        registerData.put("name", req.getParameter("name"));
        registerData.put("carType", req.getParameter("carType"));
        registerData.put("carModel", req.getParameter("carModel"));
        registerData.put("carNumber", req.getParameter("carNumber"));

        try {
            Object accessTokenObj = req.getSession().getAttribute("accessToken");
            User user = null;
            String accessToken = "";

            if (accessTokenObj != null) {
                accessToken = String.valueOf(accessTokenObj);
                user = userService.updateUser(registerData, accessToken);

            } else {
                user = userService.registerDriver(registerData);
                accessToken = userService.login(user.getPhone(), user.getPass());

                HttpSession session = req.getSession(true);
                session.setAttribute("inSystem", true);
                session.setAttribute("accessToken", accessToken);
                session.setAttribute("currentUserName", user.getName());
            }

            resp.getWriter().write("SUCCESS");

        } catch (RegisterException e) {
            LOG.error(e);
            resp.getWriter().write("This phone using already");

        } catch (Exception e) {
            LOG.error(e);
            resp.getWriter().write("Incorrect registration data");
        }
    }
}


