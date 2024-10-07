package ru.javawebinar.topjava.repositories;

import ru.javawebinar.topjava.model.Meal;

import java.util.*;

public interface MealRepository {
    Meal save(Meal meal);

    Optional<Meal> findById(int id);

    List<Meal> findAll();

    void delete(int id);
}
