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
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static final int NONEXISTENT_RECORD_ID = 99999;

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID, USER_ID);
        assertMatch(meal, MealTestData.userMeal);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, NONEXISTENT_RECORD_ID));
        assertThrows(NotFoundException.class, () -> service.get(NONEXISTENT_RECORD_ID, USER_ID));
    }


    @Test
    public void delete() {
        service.delete(MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID, NONEXISTENT_RECORD_ID));
        assertThrows(NotFoundException.class, () -> service.delete(NONEXISTENT_RECORD_ID, USER_ID));
    }

    @Test
    public void update() {
        Meal changedMeal = new Meal(userMeal);
        changedMeal.setDateTime(LocalDateTime.parse("2024-10-22T20:02"));
        changedMeal.setDescription("Updated Meal");
        changedMeal.setCalories(changedMeal.getCalories() + 1);

        service.update(changedMeal, USER_ID);
        assertMatch(service.get(changedMeal.getId(), USER_ID), changedMeal);
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
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, MealTestData.userMeal.getDateTime(), "Duplicate", 11), USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        // skipped
    }

    @Test
    public void getAll() {
        // skipped
    }
}