package ua.artcode.taxi.servlets.notAjax;

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

@WebServlet(urlPatterns = {"/user-info"})
public class UserInfoMenuServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(UserInfoMenuServlet.class);

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

            req.getServletContext().getRequestDispatcher("/WEB-INF/pages/user-info.jsp").include(req, resp);

        } catch (Exception e) {
            req.setAttribute("errorTitle", "Login Error");
            req.setAttribute("errorMessage", "invalid name");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        }
    }

}
