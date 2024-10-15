package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public interface MealRepository extends BaseCRUDRepository<Meal> {
    List<Meal> getAll(Predicate<Meal> filterBy, Comparator<Meal> sortBy);

    Meal getByIdAndUserId(int id, int userId);
}
