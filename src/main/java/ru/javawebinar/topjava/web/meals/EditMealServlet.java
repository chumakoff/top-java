package ru.javawebinar.topjava.web.meals;

import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class EditMealServlet extends BaseMealServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<Meal> mealOpt = findMeal(request);
        if (!mealOpt.isPresent()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("meal", mealOpt.get());
        request.getRequestDispatcher("/WEB-INF/meals/edit.jsp").forward(request, response);
    }
}
