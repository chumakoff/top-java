package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestHelper.assertMatch;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static final int NONEXISTENT_RECORD_ID = 99999;
    private static final int USER_ID = UserTestData.USER_ID;
    private static final int ADMIN_ID = UserTestData.ADMIN_ID;
    private static final int GUEST_ID = UserTestData.GUEST_ID;

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL_ID, USER_ID);
        assertMatch(meal, MealTestData.userMeal);
    }

    @Test
    public void get_throwsNotFoundWhenMealDoesNotBelongToUser() {
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_ID, ADMIN_ID));
    }

    @Test
    public void get_throwsNotFoundWhenNonexistentMealId() {
        assertThrows(NotFoundException.class, () -> service.get(NONEXISTENT_RECORD_ID, USER_ID));
    }

    @Test
    public void get_throwsNotFoundWhenNonexistentUserId() {
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_ID, NONEXISTENT_RECORD_ID));
    }


    @Test
    public void delete() {
        service.delete(USER_MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_ID, USER_ID));
    }

    @Test
    public void delete_throwsNotFoundWhenMealDoesNotBelongToUser() {
        assertThrows(NotFoundException.class, () -> service.delete(USER_MEAL_ID, ADMIN_ID));
    }

    @Test
    public void delete_throwsNotFoundWhenNonexistentMealId() {
        assertThrows(NotFoundException.class, () -> service.delete(NONEXISTENT_RECORD_ID, USER_ID));
    }

    @Test
    public void delete_throwsNotFoundWhenNonexistentUserId() {
        assertThrows(NotFoundException.class, () -> service.delete(USER_MEAL_ID, NONEXISTENT_RECORD_ID));
    }

    @Test
    public void update() {
        Meal changedMeal = new Meal(userMeal);
        changedMeal.setDateTime(LocalDateTime.parse("2024-10-22T20:02"));
        changedMeal.setDescription("Updated Meal");
        changedMeal.setCalories(changedMeal.getCalories() + 1);

        service.update(new Meal(changedMeal), USER_ID);
        assertMatch(service.get(changedMeal.getId(), USER_ID), changedMeal);
    }

    @Test
    public void update_throwsExceptionWhenMealDoesNotBelongToUser() {
        Meal mealBeforeUpdate = new Meal(userMeal);

        Meal changedMeal = new Meal(userMeal);
        changedMeal.setDateTime(LocalDateTime.parse("2024-10-22T20:02"));
        changedMeal.setDescription("Updated Meal");
        changedMeal.setCalories(changedMeal.getCalories() + 1);

        assertThrows(NotFoundException.class, () -> service.update(changedMeal, ADMIN_ID));
        assertMatch(service.get(changedMeal.getId(), USER_ID), mealBeforeUpdate);
    }

    @Test
    public void create() {
        Meal createdMeal = service.create(getNew(), USER_ID);
        Integer newId = createdMeal.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(createdMeal, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void create_throwsExceptionWhenDuplicateDateTime() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, MealTestData.userMeal.getDateTime(), "Duplicate", 11), USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(LocalDate.parse("2024-10-21"), LocalDate.parse("2024-10-22"), GUEST_ID);
        assertMatch(meals, guestMeal1);

        meals = service.getBetweenInclusive(LocalDate.parse("2024-10-23"), LocalDate.parse("2024-10-24"), GUEST_ID);
        assertMatch(meals, guestMeal3, guestMeal2);
    }

    @Test
    public void getAll_returnsMealsForGivenUser() {
        List<Meal> allUserMeals = service.getAll(USER_ID);
        assertMatch(allUserMeals, userMeal);

        List<Meal> allAdminMeals = service.getAll(ADMIN_ID);
        assertMatch(allAdminMeals, adminMeal);
    }

    @Test
    public void getAll_returnsMealsOrderedByDateTime() {
        List<Meal> allGuestMeals = service.getAll(GUEST_ID);
        assertMatch(allGuestMeals, guestMeal3, guestMeal2, guestMeal1);
    }
}