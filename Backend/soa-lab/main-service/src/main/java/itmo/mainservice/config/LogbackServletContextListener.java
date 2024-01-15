package itmo.mainservice.config;

import ch.qos.logback.classic.LoggerContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.LoggerFactory;

@WebListener
public class LogbackServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.stop();
    }

}
