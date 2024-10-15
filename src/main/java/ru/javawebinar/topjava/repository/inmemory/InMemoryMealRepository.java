package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

@Repository
public class InMemoryMealRepository extends BaseInMemoryRepository<Meal> implements MealRepository {
    {
        MealsUtil.meals.forEach(this::save);
    }

    public Meal getByIdAndUserId(int id, int userId) {
        Meal meal = get(id);
        return meal.getUserId() == userId ? meal : null;
    }
}

