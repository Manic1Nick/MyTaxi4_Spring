package ua.artcode.taxi.utils;

import ua.artcode.taxi.dao.*;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.service.UserServiceJdbcImpl;
import ua.artcode.taxi.service.ValidatorJdbcImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeansFactory {

    public static Map<String, Object> objectMap = new ConcurrentHashMap<>();

    //todo singleton with multithreading
    public synchronized static UserService createUserService() {

        if (objectMap.containsKey("userService")) {
            return (UserService) objectMap.get("userService");

        } else {
            AddressDao addressDao = new AddressDao();
            CarDao carDao = new CarDao();
            UserDao userDao = new UserJdbcDao(addressDao, carDao);
            OrderDao orderDao = new OrderJdbcDao(userDao, addressDao);
            ValidatorJdbcImpl validator = new ValidatorJdbcImpl(userDao);

            return new UserServiceJdbcImpl(userDao, orderDao, validator);
        }


    }
}
