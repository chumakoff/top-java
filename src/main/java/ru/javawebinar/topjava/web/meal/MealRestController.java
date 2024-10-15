package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.util.exception.UnauthenticatedException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;
import java.util.Objects;

@Controller
public class MealRestController {
    @Autowired
    private MealService mealService;

    @Autowired
    private UserService userService;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public List<MealTo> getAll() {
        log.info("Get all meals");

        User currentUser = authenticate();

        return MealsUtil.getTos(
                mealService.getAllForUser(currentUser.getId()),
                currentUser.getCaloriesPerDay()
        );
    }

    public Meal get(int id) {
        log.info("Get meal {}", id);

        User currentUser = authenticate();
        return findUserMealOrThrowNotFound(currentUser, id);
    }

    public Meal create(Meal meal) {
        log.info("Create meal {}", meal);

        User currentUser = authenticate();
        meal.setUserId(currentUser.getId());
        return mealService.create(meal);
    }

    public void update(Meal mealAttrs) {
        log.info("Update meal {}", mealAttrs);

        User currentUser = authenticate();
        Meal meal = findUserMealOrThrowNotFound(currentUser, mealAttrs.getId());

        meal.setDateTime(mealAttrs.getDateTime());
        meal.setDescription(mealAttrs.getDescription());
        meal.setCalories(mealAttrs.getCalories());

        mealService.update(meal);
    }

    public void delete(int id) {
        log.info("Delete meal {}", id);

        User currentUser = authenticate();
        Meal meal = findUserMealOrThrowNotFound(currentUser, id);
        mealService.delete(meal);
    }

    private User authenticate() {
        User user = userService.get(SecurityUtil.authUserId());
        if (user == null) throw new UnauthenticatedException();
        return user;
    }

    private Meal findUserMealOrThrowNotFound(User user, int mealId) {
        Meal meal = mealService.get(mealId);

        if (meal == null || !Objects.equals(meal.getUserId(), user.getId())) {
            throw new NotFoundException("Meal not found: id=" + mealId + ", userId=" + user.getId());
        }
        return meal;
    }
}