package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
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
        System.out.println(filteredByStreamsOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesPerDay = new HashMap<>();
        for (UserMeal meal : meals) totalCaloriesPerDay.merge(dayIndex(meal), meal.getCalories(), Integer::sum);

        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();

        for (UserMeal meal : meals) {
            if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                mealsWithExcess.add(toUserMealWithExcess(meal, totalCaloriesPerDay.get(dayIndex(meal)) > caloriesPerDay));
        }

        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesPerDay = meals.stream().collect(
                Collectors.toMap(UserMealsUtil::dayIndex, UserMeal::getCalories, Integer::sum)
        );

        return meals.stream()
                .filter(meal -> isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> toUserMealWithExcess(meal, totalCaloriesPerDay.get(dayIndex(meal)) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreamsOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream().collect(
                Collectors.groupingBy(
                        meal -> dayIndex(meal),
                        new DailyMealsAggregationCollector(startTime, endTime, caloriesPerDay)
                )
        ).values().stream().flatMap(value -> value.getMealsWithExcess().stream()).collect(Collectors.toList());
    }

    private static UserMealWithExcess toUserMealWithExcess(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    private static LocalDate dayIndex(UserMeal meal) {
        return meal.getDateTime().toLocalDate();
    }

    public static class DailyMealsAggregation {
        private LocalTime startTime;
        private LocalTime endTime;
        private List<UserMeal> mealsInTimeRange = new ArrayList<>();
        private int totalCalories = 0;
        private int maxCalories = 0;

        DailyMealsAggregation(LocalTime startTime, LocalTime endTime, int maxCalories) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.maxCalories = maxCalories;
        }

        public void takeMealIntoAccount(UserMeal meal) {
            totalCalories += meal.getCalories();
            if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) mealsInTimeRange.add(meal);
        }

        public List<UserMealWithExcess> getMealsWithExcess() {
            return mealsInTimeRange.stream().map(meal -> toUserMealWithExcess(meal, isExcess())).collect(toList());
        }

        private Boolean isExcess() {
            return totalCalories > maxCalories;
        }
    }

    public static class DailyMealsAggregationCollector implements Collector<UserMeal, DailyMealsAggregation, DailyMealsAggregation> {
        private LocalTime startTime;
        private LocalTime endTime;
        private int maxCalories;

        DailyMealsAggregationCollector(LocalTime startTime, LocalTime endTime, int maxCalories) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.maxCalories = maxCalories;
        }

        @Override
        public Supplier<DailyMealsAggregation> supplier() {
            return () -> new DailyMealsAggregation(startTime, endTime, maxCalories);
        }

        @Override
        public BiConsumer<DailyMealsAggregation, UserMeal> accumulator() {
            return DailyMealsAggregation::takeMealIntoAccount;
        }

        @Override
        public BinaryOperator<DailyMealsAggregation> combiner() {
            return (aggr1, aggr2) -> {
                aggr1.mealsInTimeRange.addAll(aggr2.mealsInTimeRange);
                aggr1.totalCalories += aggr2.totalCalories;
                return aggr1;
            };
        }

        @Override
        public Function<DailyMealsAggregation, DailyMealsAggregation> finisher() {
            return aggr -> aggr;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }
    }
}
