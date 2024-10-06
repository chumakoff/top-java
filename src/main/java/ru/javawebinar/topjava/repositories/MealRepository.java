package ru.javawebinar.topjava.repositories;

import ru.javawebinar.topjava.model.Meal;

import java.util.*;

public interface MealRepository {
    void save(Meal meal);

    Optional<Meal> findById(Integer id);

    List<Meal> findAll();

    void delete(Meal meal);
}
