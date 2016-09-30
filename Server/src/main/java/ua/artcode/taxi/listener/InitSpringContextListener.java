package ua.artcode.taxi.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InitSpringContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("APP_NAME", "app/ajax");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
