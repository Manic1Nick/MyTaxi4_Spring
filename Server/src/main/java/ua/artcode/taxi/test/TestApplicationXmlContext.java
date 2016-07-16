package ua.artcode.taxi.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.UserService;

public class TestApplicationXmlContext {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        UserService service = context.getBean(UserService.class);

        try {
            String accessKey = service.login("1234","test");
            System.out.println(accessKey);
            User user = service.getUser(accessKey);
            System.out.println(user);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}