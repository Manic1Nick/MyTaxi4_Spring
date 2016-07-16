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

@WebServlet(urlPatterns = {"/register-driver"})
public class RegisterDriverServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(RegisterDriverServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));

        if (accessToken!= null) {
            User user = userService.getUser(accessToken);
            req.setAttribute("user", user);
        }

        req.getRequestDispatcher("/WEB-INF/pages/register-driver.jsp").forward(req,resp);
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
            String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));
            User user = null;

            if (accessToken != null) {
                user = userService.updateUser(registerData, accessToken);

            } else {
                user = userService.registerPassenger(registerData);
                accessToken = userService.login(user.getPhone(), user.getPass());

                HttpSession session = req.getSession(true);
                session.setAttribute("inSystem", true);
                session.setAttribute("accessToken", accessToken);
                session.setAttribute("currentUserName", user.getName());
            }

            req.setAttribute("user", user);
            req.getRequestDispatcher("/WEB-INF/pages/user-info.jsp").forward(req, resp);

        } catch (RegisterException e) {
            LOG.error(e);
            req.setAttribute("error", "This phone using already");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        } catch (Exception e) {
            LOG.error(e);
            req.setAttribute("errorTitle", "Login Error");
            req.setAttribute("errorMessage", "invalid data");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        }
    }
}


