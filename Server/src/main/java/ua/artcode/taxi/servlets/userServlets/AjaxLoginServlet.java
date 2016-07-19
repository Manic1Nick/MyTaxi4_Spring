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

@WebServlet(urlPatterns = {"/ajax/login"})
public class AjaxLoginServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(AjaxLoginServlet.class);

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        session.setAttribute("accessToken", null);
        session.invalidate();

        req.getRequestDispatcher("/WEB-INF/pages/ajax-login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String phone = req.getParameter("phone");
        String pass = req.getParameter("password");

        try {
            String accessToken = userService.login(phone, pass);
            User found = userService.getUser(accessToken);

            HttpSession session = req.getSession(true);
            session.setAttribute("inSystem", true);
            session.setAttribute("accessToken", accessToken);
            session.setAttribute("currentUserName", found.getName());

            resp.getWriter().write("SUCCESS");

        } catch (Exception e) {
            LOG.error(e);
            resp.getWriter().write("Incorrect name or password");
        }
    }
}

