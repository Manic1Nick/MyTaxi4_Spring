package ua.artcode.taxi.servlets.userServlets;

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

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            User user = userService.deleteUser(String.valueOf(req.getAttribute("accessToken")));

            HttpSession session = req.getSession();
            session.invalidate();
            resp.getWriter().write("id:" + user.getId());

        } catch (WrongStatusOrderException e) {
            resp.getWriter().write(e.getMessage());
        }
    }
}
