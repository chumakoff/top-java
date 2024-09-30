package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<Integer, Integer> totalCaloriesPerDay = new HashMap<>();
        List<UserMeal> filteredMeals = new ArrayList<>();

        // Filter meals by the given time range
        for (UserMeal meal : meals) {
            if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) filteredMeals.add(meal);

            // Accumulate total daily calories while filtering
            var currentCalories = totalCaloriesPerDay.getOrDefault(meal.getDay(), 0);
            totalCaloriesPerDay.put(meal.getDay(), currentCalories + meal.getCalories());
        }

        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();

        for (UserMeal meal : filteredMeals) {
            mealsWithExcess.add(
                    toUserMealWithExcess(meal, totalCaloriesPerDay.get(meal.getDay()) > caloriesPerDay)
            );
        }

        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<Integer, Integer> totalCaloriesPerDay = new HashMap<>();

        // Filter meals by the given time range
        var filteredMeals = meals.stream().filter(meal -> {
            // Accumulate total daily calories while filtering
            var currentCalories = totalCaloriesPerDay.getOrDefault(meal.getDay(), 0);
            totalCaloriesPerDay.put(meal.getDay(), currentCalories + meal.getCalories());

            return isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime);
        }).toList();

        return filteredMeals.stream().map(
                meal -> toUserMealWithExcess(meal, totalCaloriesPerDay.get(meal.getDay()) > caloriesPerDay)
        ).toList();
    }

    private static UserMealWithExcess toUserMealWithExcess(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
