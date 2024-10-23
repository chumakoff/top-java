package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestHelper {
    public static void assertMatch(Meal actualMeal, Meal expectedMeal) {
        assertThat(actualMeal).usingRecursiveComparison().isEqualTo(expectedMeal);
    }

    public static void assertMatch(Iterable<Meal> actualMeals, Meal... expectedMeals) {
        assertThat(actualMeals).usingRecursiveFieldByFieldElementComparatorIgnoringFields().isEqualTo(Arrays.asList(expectedMeals));
    }
}
