package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

@Controller
public class MealRestController {
    @Autowired
    private MealService mealService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public List<MealTo> getAll() {
        log.info("Get all meals");
        return MealsUtil.getTos(mealService.getAll(), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        log.info("Get meal {}", id);
        return mealService.get(id);
    }

    public Meal create(Meal meal) {
        log.info("Create meal {}", meal);
        return mealService.create(meal);
    }

    public void update(Meal meal, int id) {
        log.info("Update meal {} with id={}", meal, id);
        mealService.update(meal, id);
    }

    public void delete(int id) {
        log.info("Delete meal {}", id);
        mealService.delete(id);
    }
}