package ru.javawebinar.topjava.repositories;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryMealRepository implements MealRepository {
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    private final ConcurrentMap<Integer, Meal> meals;

    public InMemoryMealRepository() {
        this.meals = new ConcurrentHashMap<>();

        Stream.of(
                new Meal(LocalDateTime.parse("2020-01-29T12:00"), "Lunch", 2001),
                new Meal(LocalDateTime.parse("2020-01-30T10:00"), "Breakfast", 400),
                new Meal(LocalDateTime.parse("2020-01-30T13:00"), "Lunch", 1000),
                new Meal(LocalDateTime.parse("2020-01-30T20:00"), "Dinner", 600),
                new Meal(LocalDateTime.parse("2020-01-31T00:00"), "Second dinner", 400),
                new Meal(LocalDateTime.parse("2020-01-31T07:00"), "Breakfast", 400),
                new Meal(LocalDateTime.parse("2020-01-31T10:00"), "Second Breakfast", 400),
                new Meal(LocalDateTime.parse("2020-01-31T13:00"), "Lunch", 400),
                new Meal(LocalDateTime.parse("2020-01-31T20:00"), "Dinner", 401),
                new Meal(LocalDateTime.parse("2020-03-30T10:00"), "Breakfast", 9999),
                new Meal(LocalDateTime.parse("2021-01-30T10:00"), "Breakfast", 2000)
        ).forEach(this::save);
    }

    public Meal save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(idGenerator.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        } else {
            Meal oldValue = meals.replace(meal.getId(), meal);
            return oldValue == null ? null : meal;
        }
    }

    public Optional<Meal> findById(int id) {
        Meal meal = meals.get(id);
        if (meal == null) return Optional.empty();

        return Optional.of(new Meal(meal));
    }

    public List<Meal> findAll() {
        return meals.values().stream().map(Meal::new).collect(Collectors.toList());
    }

    public void delete(int id) {
        meals.remove(id);
    }
}
