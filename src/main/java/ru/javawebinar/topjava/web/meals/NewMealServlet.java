package ru.javawebinar.topjava.web.meals;

import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class NewMealServlet extends BaseMealServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("meal", new Meal(null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0));
        request.getRequestDispatcher("/WEB-INF/meals/new.jsp").forward(request, response);
    }
}
