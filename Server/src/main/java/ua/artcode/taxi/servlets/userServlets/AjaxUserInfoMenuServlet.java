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

@WebServlet(urlPatterns = {"/ajax/user-info"})
public class AjaxUserInfoMenuServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(AjaxUserInfoMenuServlet.class);

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

            /*String userToJSON = ReflectionFormatter.userToJSON(found);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(userToJSON);*/

            req.getServletContext().getRequestDispatcher("/WEB-INF/pages/ajax-user-info.jsp").include(req, resp);

        } catch (Exception e) {
            LOG.error(e);
            resp.getWriter().write("Incorrect data");
        }
    }

}
