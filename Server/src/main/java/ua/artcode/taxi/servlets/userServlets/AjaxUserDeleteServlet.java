package ua.artcode.taxi.servlets.userServlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.exception.WrongStatusOrderException;
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

@WebServlet(urlPatterns = {"/ajax/user/delete"})
public class AjaxUserDeleteServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(AjaxUserDeleteServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));

        User user = userService.getUser(accessToken);

        resp.getWriter().write("id:" + user.getId());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));

            User user = userService.deleteUser(accessToken);

            HttpSession session = req.getSession();
            session.invalidate();

            LOG.info("Successful attempt to delete user ID=" + user.getId());

            resp.getWriter().write("id:" + user.getId());

        } catch (WrongStatusOrderException e) {
            LOG.error(e);
            resp.getWriter().write("Can't delete. You have orders with status NEW or IN_PROGRESS");
        }
    }
}
