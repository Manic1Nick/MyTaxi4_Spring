package ua.artcode.taxi.servlets.userServlets;

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

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Object accessTokenObj = req.getAttribute("accessToken");

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
        registerData.put("pass", req.getParameter("password"));
        registerData.put("name", req.getParameter("name"));
        registerData.put("carType", req.getParameter("carType"));
        registerData.put("carModel", req.getParameter("carModel"));
        registerData.put("carNumber", req.getParameter("carNumber"));

        try {
            Object accessTokenObj = req.getAttribute("accessToken");

            if (accessTokenObj != null) {
                userService.updateUser(registerData, String.valueOf(accessTokenObj));

            } else {
                User user = userService.registerDriver(registerData);
                String accessToken = userService.login(user.getPhone(), user.getPass());

                HttpSession session = req.getSession(true);
                session.setAttribute("inSystem", true);
                session.setAttribute("accessToken", accessToken);
                session.setAttribute("currentUserID", user.getId());
            }
            resp.getWriter().write("SUCCESS");

        } catch (Exception e) {
            resp.getWriter().write(e.getMessage());
        }
    }
}


