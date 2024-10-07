package ru.javawebinar.topjava.listeners;

import ru.javawebinar.topjava.repositories.InMemoryMealRepository;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute("mealRepository", new InMemoryMealRepository());
    }
}