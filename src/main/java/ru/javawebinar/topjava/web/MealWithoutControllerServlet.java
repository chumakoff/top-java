package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.util.exception.UnauthenticatedException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealWithoutControllerServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealService mealService;
    private UserService userService;

    @Override
    public void init() {
        mealService = new MealService(new InMemoryMealRepository());
        userService = new UserService(new InMemoryUserRepository());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        User currentUser = authenticate();

        Meal meal;
        String idString = request.getParameter("id");

        if (idString != null && !idString.isEmpty()) {
            meal = findUserMealOrThrowNotFound(currentUser, Integer.parseInt(idString));
        } else {
            meal = new Meal();
            meal.setUserId(currentUser.getId());
        }

        meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime")));
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));

        if (meal.isNew()) {
            log.info("Create {}", meal);
            mealService.create(meal);
        } else {
            log.info("Update {}", meal);
            mealService.update(meal);
        }

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = authenticate();

        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                Meal meal = findUserMealOrThrowNotFound(currentUser, getId(request));
                log.info("Delete {}", meal);
                mealService.delete(meal);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal mealForForm = "create".equals(action) ?
                        new Meal(currentUser.getId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealService.get(getId(request));
                request.setAttribute("meal", mealForForm);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute(
                        "meals",
                        MealsUtil.getTos(
                                mealService.getAllForUser(currentUser.getId()),
                                currentUser.getCaloriesPerDay()
                        )
                );
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private User authenticate() {
        User user = userService.get(SecurityUtil.authUserId());
        if (user == null) throw new UnauthenticatedException();
        return user;
    }

    private Meal findUserMealOrThrowNotFound(User user, int mealId) {
        Meal meal = mealService.get(mealId);
        if (meal == null || meal.getUserId() != user.getId()) {
            throw new NotFoundException("Meal not found: id=" + mealId);
        }
        return meal;
    }
}

