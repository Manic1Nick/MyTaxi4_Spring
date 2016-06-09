package ua.artcode.taxi.run;

import ua.artcode.taxi.dao.*;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.service.UserServiceImpl;
import ua.artcode.taxi.service.ValidatorImpl;
import ua.artcode.taxi.view.UserLogin;

public class StartApp {


    public static void main(String[] args) {

        AppDB appDB = new AppDB();
        UserDao userDao = new UserDaoInnerDbImpl(appDB);
        OrderDao orderDao = new OrderDaoInnerDbImpl(appDB);
        ValidatorImpl validator = new ValidatorImpl(appDB);

        UserService userService = new UserServiceImpl(userDao, orderDao, validator);
        new UserLogin(userService);

    }
}
