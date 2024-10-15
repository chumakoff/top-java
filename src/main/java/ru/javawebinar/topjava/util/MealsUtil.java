package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static final List<Meal> meals = generateMeals();

    public static List<Meal> generateMeals() {
        UserRepository userRepository = new InMemoryUserRepository();
        int regularUserId = userRepository.getByEmail("user@user.user").getId();
        int adminUserId = userRepository.getByEmail("admin@admin.admin").getId();
        List<Meal> meals = new ArrayList<>();

        Arrays.asList(regularUserId, adminUserId).forEach(userId -> {
            meals.addAll(Arrays.asList(
                    new Meal(userId, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак " + userId, 500),
                    new Meal(userId, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед " + userId, 1000),
                    new Meal(userId, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин " + userId, 500),
                    new Meal(userId, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение " + userId, 100),
                    new Meal(userId, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак " + userId, 1000),
                    new Meal(userId, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед " + userId, 500),
                    new Meal(userId, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин " + userId, 410)
            ));
        });

        return meals;
    }

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return filterByPredicate(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTos(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return filterByPredicate(meals, caloriesPerDay, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime));
    }

    private static List<MealTo> filterByPredicate(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
