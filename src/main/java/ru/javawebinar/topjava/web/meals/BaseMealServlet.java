package ru.javawebinar.topjava.web.meals;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repositories.MealRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

abstract class BaseMealServlet extends HttpServlet {
    protected MealRepository getMealRepository() {
        return (MealRepository) this.getServletConfig().getServletContext().getAttribute("mealRepository");
    }

    protected Optional<Meal> findMeal(HttpServletRequest request) {
        int mealId = Integer.parseInt(request.getParameter("id"));
        return getMealRepository().findById(mealId);
    }
}