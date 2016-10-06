package ua.artcode.taxi.servlets.userServlets;

import ua.artcode.taxi.model.Address;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.utils.BeansFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/ajax/user/get-location"})
public class AjaxGetUserLocationServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Address userLocation = userService.getUserLocation();
            resp.getWriter().write(userLocation.separateByCommas(userLocation));

        } catch (Exception e) {
            resp.getWriter().write(e.getMessage());
        }
    }

}
