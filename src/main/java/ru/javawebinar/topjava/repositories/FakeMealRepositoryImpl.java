package ru.javawebinar.topjava.repositories;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FakeMealRepositoryImpl implements MealRepository {
    private final AtomicInteger fakeIdGenerator = new AtomicInteger(1);
    private final ConcurrentMap<Integer, Meal> fakeMeals;

    public FakeMealRepositoryImpl() {
        this.fakeMeals = Stream.of(
                new Meal(fakeIdGenerator.incrementAndGet(), LocalDateTime.parse("2020-01-29T12:00"), "Lunch", 2001),
                new Meal(fakeIdGenerator.incrementAndGet(), LocalDateTime.parse("2020-01-30T10:00"), "Breakfast", 400),
                new Meal(fakeIdGenerator.incrementAndGet(), LocalDateTime.parse("2020-01-30T13:00"), "Lunch", 1000),
                new Meal(fakeIdGenerator.incrementAndGet(), LocalDateTime.parse("2020-01-30T20:00"), "Dinner", 600),
                new Meal(fakeIdGenerator.incrementAndGet(), LocalDateTime.parse("2020-01-31T00:00"), "Second dinner", 400),
                new Meal(fakeIdGenerator.incrementAndGet(), LocalDateTime.parse("2020-01-31T07:00"), "Breakfast", 400),
                new Meal(fakeIdGenerator.incrementAndGet(), LocalDateTime.parse("2020-01-31T10:00"), "Second Breakfast", 400),
                new Meal(fakeIdGenerator.incrementAndGet(), LocalDateTime.parse("2020-01-31T13:00"), "Lunch", 400),
                new Meal(fakeIdGenerator.incrementAndGet(), LocalDateTime.parse("2020-01-31T20:00"), "Dinner", 401),
                new Meal(fakeIdGenerator.incrementAndGet(), LocalDateTime.parse("2020-03-30T10:00"), "Breakfast", 9999),
                new Meal(fakeIdGenerator.incrementAndGet(), LocalDateTime.parse("2021-01-30T10:00"), "Breakfast", 2000)
        ).collect(
                Collectors.toConcurrentMap(Meal::getId, Function.identity(), (first, second) -> second)
        );
    }

    public void save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(fakeIdGenerator.incrementAndGet());
        }
        fakeMeals.put(meal.getId(), meal);
    }

    public Optional<Meal> findById(Integer id) {
        Meal meal = fakeMeals.get(id);
        if (meal == null) return Optional.empty();
        return Optional.of(meal.copy());
    }

    public List<Meal> findAll() {
        return fakeMeals.values().stream().map(Meal::copy).collect(Collectors.toList());
    }

    public void delete(Meal meal) {
        fakeMeals.remove(meal.getId());
    }
}
