package ru.javawebinar.topjava.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.UserMeal;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javawebinar.topjava.util.UserMealsUtil.filteredByCycles;

class UserMealsUtilTest {
    private static List<UserMeal> meals;

    @BeforeAll
    static void initAll() {
        meals = Arrays.asList(
                new UserMeal(LocalDateTime.parse("2020-01-28T12:00"), "28_12", 2001),
                new UserMeal(LocalDateTime.parse("2020-01-29T11:00"), "29_11", 2001),
                new UserMeal(LocalDateTime.parse("2020-01-30T10:00"), "30_10", 400),
                new UserMeal(LocalDateTime.parse("2020-01-30T13:00"), "30_13", 1000),
                new UserMeal(LocalDateTime.parse("2020-01-30T20:00"), "30_20", 600),
                new UserMeal(LocalDateTime.parse("2020-01-31T00:00"), "31_00", 400),
                new UserMeal(LocalDateTime.parse("2020-01-31T07:00"), "31_07", 400),
                new UserMeal(LocalDateTime.parse("2020-01-31T10:00"), "31_10", 400),
                new UserMeal(LocalDateTime.parse("2020-01-31T13:00"), "31_13", 400),
                new UserMeal(LocalDateTime.parse("2020-01-31T20:00"), "31_20", 401)
        );
    }

    @Test
    void filteredByCyclesTest() {
        var mealsWithExcess = filteredByCycles(meals, LocalTime.parse("07:00"), LocalTime.parse("12:00"), 2000);
        assertEquals(mealsWithExcess.size(), 4);

        assertEquals(mealsWithExcess.get(0).getDescription(), "29_11");
        assertTrue(mealsWithExcess.get(0).isExcess());

        assertEquals(mealsWithExcess.get(1).getDescription(), "30_10");
        assertFalse(mealsWithExcess.get(1).isExcess());

        assertEquals(mealsWithExcess.get(2).getDescription(), "31_07");
        assertTrue(mealsWithExcess.get(2).isExcess());

        assertEquals(mealsWithExcess.get(3).getDescription(), "31_10");
        assertTrue(mealsWithExcess.get(3).isExcess());
    }

    @Test
    void filteredByStreamsTest() {
        var mealsWithExcess = filteredByCycles(meals, LocalTime.parse("07:00"), LocalTime.parse("12:00"), 2000);
        assertEquals(mealsWithExcess.size(), 4);

        assertEquals(mealsWithExcess.get(0).getDescription(), "29_11");
        assertTrue(mealsWithExcess.get(0).isExcess());

        assertEquals(mealsWithExcess.get(1).getDescription(), "30_10");
        assertFalse(mealsWithExcess.get(1).isExcess());

        assertEquals(mealsWithExcess.get(2).getDescription(), "31_07");
        assertTrue(mealsWithExcess.get(2).isExcess());

        assertEquals(mealsWithExcess.get(3).getDescription(), "31_10");
        assertTrue(mealsWithExcess.get(3).isExcess());
    }
}