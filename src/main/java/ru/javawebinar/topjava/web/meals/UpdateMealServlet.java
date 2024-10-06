package ru.javawebinar.topjava.web.meals;

import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@WebServlet(name = "UpdateMealsServlet", urlPatterns = "/meals/update")
public class UpdateMealServlet extends BaseMealServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<Meal> mealOpt = findMeal(request);
        if (!mealOpt.isPresent()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Meal meal = mealOpt.get();
        meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime")));
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));
        getMealRepository().save(meal);

        response.sendRedirect(request.getContextPath() + "/meals");
    }
}