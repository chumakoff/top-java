package ru.javawebinar.topjava.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javawebinar.topjava.util.UserMealsUtil.filteredByCycles;
import static ru.javawebinar.topjava.util.UserMealsUtil.filteredByStreamsOptional2;

class UserMealsUtilTest {
    private static List<UserMeal> meals;

    @BeforeAll
    static void initAll() {
        meals = Arrays.asList(
                new UserMeal(LocalDateTime.parse("2020-01-29T12:00"), "2020-01-28_12", 2001),
                new UserMeal(LocalDateTime.parse("2020-01-30T10:00"), "2020-01-30_10", 400),
                new UserMeal(LocalDateTime.parse("2020-01-30T13:00"), "2020-01-30_13", 1000),
                new UserMeal(LocalDateTime.parse("2020-01-30T20:00"), "2020-01-30_20", 600),
                new UserMeal(LocalDateTime.parse("2020-01-31T00:00"), "2020-01-31_00", 400),
                new UserMeal(LocalDateTime.parse("2020-01-31T07:00"), "2020-01-31_07", 400),
                new UserMeal(LocalDateTime.parse("2020-01-31T10:00"), "2020-01-31_10", 400),
                new UserMeal(LocalDateTime.parse("2020-01-31T13:00"), "2020-01-31_13", 400),
                new UserMeal(LocalDateTime.parse("2020-01-31T20:00"), "2020-01-31_20", 401),
                new UserMeal(LocalDateTime.parse("2020-03-30T10:00"), "2020-03-30_10", 9999),
                new UserMeal(LocalDateTime.parse("2021-01-30T10:00"), "2021-01-30_10", 2000)
        );
    }

    @Test
    void filteredByCyclesTest() {
        List<UserMealWithExcess> mealsWithExcess = filteredByCycles(meals, LocalTime.parse("07:00"), LocalTime.parse("12:00"), 2000);
        assertEquals(mealsWithExcess.size(), 5);

        assertEquals(mealsWithExcess.get(0).getDescription(), "2020-01-30_10");
        assertFalse(mealsWithExcess.get(0).isExcess());

        assertEquals(mealsWithExcess.get(1).getDescription(), "2020-01-31_07");
        assertTrue(mealsWithExcess.get(1).isExcess());

        assertEquals(mealsWithExcess.get(2).getDescription(), "2020-01-31_10");
        assertTrue(mealsWithExcess.get(2).isExcess());

        assertEquals(mealsWithExcess.get(3).getDescription(), "2020-03-30_10");
        assertTrue(mealsWithExcess.get(3).isExcess());

        assertEquals(mealsWithExcess.get(4).getDescription(), "2021-01-30_10");
        assertFalse(mealsWithExcess.get(4).isExcess());
    }

    @Test
    void filteredByStreamsTest() {
        List<UserMealWithExcess> mealsWithExcess = filteredByCycles(meals, LocalTime.parse("07:00"), LocalTime.parse("12:00"), 2000);
        assertEquals(mealsWithExcess.size(), 5);

        assertEquals(mealsWithExcess.get(0).getDescription(), "2020-01-30_10");
        assertFalse(mealsWithExcess.get(0).isExcess());

        assertEquals(mealsWithExcess.get(1).getDescription(), "2020-01-31_07");
        assertTrue(mealsWithExcess.get(1).isExcess());

        assertEquals(mealsWithExcess.get(2).getDescription(), "2020-01-31_10");
        assertTrue(mealsWithExcess.get(2).isExcess());

        assertEquals(mealsWithExcess.get(3).getDescription(), "2020-03-30_10");
        assertTrue(mealsWithExcess.get(3).isExcess());

        assertEquals(mealsWithExcess.get(4).getDescription(), "2021-01-30_10");
        assertFalse(mealsWithExcess.get(4).isExcess());
    }

    @Test
    void filteredByStreamsOptional2Test() {
        List<UserMealWithExcess> mealsWithExcess = filteredByStreamsOptional2(meals, LocalTime.parse("07:00"), LocalTime.parse("12:00"), 2000);
        assertEquals(mealsWithExcess.size(), 5);

        UserMealWithExcess meal;

        meal = mealsWithExcess.stream().filter(m -> m.getDescription().equals("2020-01-30_10")).findFirst().get();
        assertFalse(meal.isExcess());

        meal = mealsWithExcess.stream().filter(m -> m.getDescription().equals("2020-01-31_07")).findFirst().get();
        assertTrue(meal.isExcess());

        meal = mealsWithExcess.stream().filter(m -> m.getDescription().equals("2020-01-31_10")).findFirst().get();
        assertTrue(meal.isExcess());

        meal = mealsWithExcess.stream().filter(m -> m.getDescription().equals("2020-03-30_10")).findFirst().get();
        assertTrue(meal.isExcess());

        meal = mealsWithExcess.stream().filter(m -> m.getDescription().equals("2021-01-30_10")).findFirst().get();
        assertFalse(meal.isExcess());
    }
}