package ua.artcode.taxi.servlets.notAjax;

import org.apache.log4j.Logger;
import ua.artcode.taxi.exception.WrongStatusOrderException;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.utils.BeansFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/user-delete"})
public class UserDeleteServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(UserDeleteServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));

            userService.deleteUser(accessToken);

            HttpSession session = req.getSession();
            session.invalidate();

            req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);

        } catch (WrongStatusOrderException e) {
            LOG.error(e);
            req.setAttribute("errorTitle",
                    "Can't delete user. User has orders with status NEW or IN_PROGRESS");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        }
    }
}
