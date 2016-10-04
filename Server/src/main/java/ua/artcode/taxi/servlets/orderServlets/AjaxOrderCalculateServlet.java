package ua.artcode.taxi.servlets.orderServlets;

import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.utils.BeansFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns = {"/ajax/order/calculate"})
public class AjaxOrderCalculateServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String lineFrom = req.getParameter("addressFrom");
        String lineTo = req.getParameter("addressTo");

        try {
            Map<String, Object> map = userService.calculateOrder(lineFrom, lineTo);
            resp.getWriter().print(map.get("distance") + "," + map.get("price"));

        } catch (InputDataWrongException e) {
            resp.getWriter().print(e.getMessage());
        }
    }
}
