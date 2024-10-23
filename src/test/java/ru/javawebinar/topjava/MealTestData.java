package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static ru.javawebinar.topjava.model.Meal.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_ID = START_SEQ;
    public static final int ADMIN_MEAL_ID = START_SEQ + 1;

    public static final int GUEST_MEAL_ID1 = START_SEQ + 2;
    public static final int GUEST_MEAL_ID2 = START_SEQ + 3;
    public static final int GUEST_MEAL_ID3 = START_SEQ + 4;

    public static final Meal userMeal = new Meal(USER_MEAL_ID, LocalDateTime.parse("2024-10-22T20:01"), "User Meal", 101);
    public static final Meal adminMeal = new Meal(ADMIN_MEAL_ID, LocalDateTime.parse("2024-10-22T20:02"), "Admin Meal", 102);
    public static final Meal guestMeal1 = new Meal(GUEST_MEAL_ID1, LocalDateTime.parse("2024-10-22T20:00"), "Guest Meal 1", 103);
    public static final Meal guestMeal2 = new Meal(GUEST_MEAL_ID2, LocalDateTime.parse("2024-10-23T00:00"), "Guest Meal 2", 104);
    public static final Meal guestMeal3 = new Meal(GUEST_MEAL_ID3, LocalDateTime.parse("2024-10-24T20:00"), "Guest Meal 3", 105);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.parse("2024-10-22T20:02"), "New Meal", 200);
    }
}
