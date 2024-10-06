package ru.javawebinar.topjava.web.meals;

import ru.javawebinar.topjava.repositories.FakeMealRepositoryImpl;
import ru.javawebinar.topjava.repositories.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;

@WebServlet(name = "ListMealsServlet", urlPatterns = "/meals")
public class ListMealsServlet extends BaseMealServlet {
    @Override
    public void init() {
        this.getServletConfig().getServletContext().setAttribute("mealRepository", new FakeMealRepositoryImpl());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(
                "meals",
                MealsUtil.filteredByStreams(
                        getMealRepository().findAll(),
                        LocalTime.of(0, 0),
                        LocalTime.of(23, 59),
                        2000
                )
        );
        request.getRequestDispatcher("/WEB-INF/meals/index.jsp").forward(request, response);
    }
}
