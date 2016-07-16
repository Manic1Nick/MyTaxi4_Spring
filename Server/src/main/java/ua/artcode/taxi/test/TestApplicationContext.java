package ua.artcode.taxi.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.artcode.taxi.service.UserService;

public class TestApplicationContext {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("/spring-context.xml");
        UserService service = (UserService) context.getBean("service");
        try {
            String accessKey = service.login("1234","test");
            System.out.println(accessKey);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}