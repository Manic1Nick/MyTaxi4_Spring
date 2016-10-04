package ua.artcode.taxi.servlets.userServlets;

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
public class AjaxGetUserInfoServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User found = userService.getUser(String.valueOf(req.getAttribute("accessToken")));
        req.setAttribute("user", found);
        req.getServletContext().getRequestDispatcher("/WEB-INF/pages/ajax-user-info.jsp").include(req, resp);
    }

}
