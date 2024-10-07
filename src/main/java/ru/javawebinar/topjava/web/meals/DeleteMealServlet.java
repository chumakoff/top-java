package ru.javawebinar.topjava.web.meals;

import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class DeleteMealServlet extends BaseMealServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getMealRepository().delete(getMealIdFromParams(request));
        response.sendRedirect(request.getContextPath() + "/meals");
    }
}

