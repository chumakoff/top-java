package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Comparator;
import java.util.List;

@Service
public class MealService {
    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public List<Meal> getAll() {
        return repository.getAll(
                m -> m.getUserId() == userId(),
                Comparator.comparing(Meal::getDateTime).reversed()
        );
    }

    public Meal get(int id) {
        return findUserMealOrThrowNotFound(id);
    }

    public Meal create(Meal meal) {
        meal.setUserId(userId());
        return repository.save(meal);
    }

    public void update(Meal meal, int id) {
        Meal mealRecord = findUserMealOrThrowNotFound(id);

        mealRecord.setDateTime(meal.getDateTime());
        mealRecord.setDescription(meal.getDescription());
        mealRecord.setCalories(meal.getCalories());

        repository.save(mealRecord);
    }

    public void delete(int id) {
        Meal meal = findUserMealOrThrowNotFound(id);
        repository.delete(meal.getId());
    }

    private Meal findUserMealOrThrowNotFound(int id) {
        Meal meal = repository.getByIdAndUserId(id, userId());

        if (meal == null) {
            throw new NotFoundException("Meal not found: id=" + id + ", userId=" + userId());
        }
        return meal;
    }

    private int userId() {
        return SecurityUtil.authUserId();
    }
}