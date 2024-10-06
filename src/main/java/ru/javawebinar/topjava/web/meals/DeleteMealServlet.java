package ru.javawebinar.topjava.web.meals;

import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "DeleteMealsServlet", urlPatterns = "/meals/delete")
public class DeleteMealServlet extends BaseMealServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<Meal> mealOpt = findMeal(request);
        if (!mealOpt.isPresent()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        getMealRepository().delete(mealOpt.get());
        response.sendRedirect(request.getContextPath() + "/meals");
    }
}

