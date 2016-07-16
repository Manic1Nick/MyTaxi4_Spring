package ua.artcode.taxi.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.artcode.taxi.service.UserService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeansFactory {

    public static Map<String, Object> objectMap = new ConcurrentHashMap<>();

    public synchronized static UserService createUserService() {

        if (objectMap.containsKey("userService")) {
            return (UserService) objectMap.get("userService");

        } else {
            ApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
            UserService service = context.getBean(UserService.class);

            objectMap.put("userService", service);

            return service;
        }


    }

    /*//todo singleton with multithreading
    public synchronized static UserService createUserService() {

        if (objectMap.containsKey("userService")) {
            return (UserService) objectMap.get("userService");

        } else {
            UserDao userDao = new UserJpaDao();
            OrderDao orderDao = new OrderJpaDao(userDao);
            ValidatorJdbcImpl validator = new ValidatorJdbcImpl(userDao);

            UserServiceJdbcImpl userServiceJdbc = new UserServiceJdbcImpl(userDao, orderDao, validator);
            objectMap.put("userService", userServiceJdbc);
            return userServiceJdbc;
        }


    }*/
}
