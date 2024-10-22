package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.Meal.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID = START_SEQ;

    public static final Meal userMeal = new Meal(MEAL_ID, LocalDateTime.parse("2024-10-22T20:01"), "User Meal", 101);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.parse("2024-10-22T20:02"), "New Meal", 200);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
