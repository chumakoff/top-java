package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class MealsController {
    private static final Logger log = LoggerFactory.getLogger(MealsController.class);

    @Autowired
    private MealService mealService;

    @GetMapping("/meals")
    public String listMeals(Model model, HttpServletRequest request) {
        log.info("meals list");
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        List<MealTo> meals = MealsUtil.getFilteredTos(
                mealService.getBetweenInclusive(startDate, endDate, SecurityUtil.authUserId()),
                SecurityUtil.authUserCaloriesPerDay(),
                startTime,
                endTime
        );
        request.setAttribute("meals", meals);
        return "meals";
    }

    @GetMapping("/meals/new")
    public String newMeal(Model model) {
        log.info("new meal");
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/meals")
    public String createMeal(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("create meal");
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal();
        assignRequestParams(meal, request);
        Meal createdMeal = mealService.create(meal, SecurityUtil.authUserId());
        model.addAttribute("meal", createdMeal);
        return "redirect:/meals";
    }

    @GetMapping("/meals/{id}/edit")
    public String editMeal(@PathVariable(value = "id") int id, Model model) {
        log.info("edit meal {}", id);
        Meal meal = findMealById(id);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/meals/{id}") // TODO: should be @PatchMapping
    public String updateMeal(@PathVariable(value = "id") int id, HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("update meal {}", id);
        request.setCharacterEncoding("UTF-8");
        Meal meal = findMealById(id);
        assignRequestParams(meal, request);
        mealService.update(meal, SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    @GetMapping("/meals/{id}/delete") // TODO: should be @DeleteMapping
    public String deleteMeal(@PathVariable(value = "id") int id) {
        log.info("delete meal {}", id);
        handleNotFound(() -> mealService.delete(id, SecurityUtil.authUserId()));
        return "redirect:/meals";
    }

    private Meal findMealById(int id) {
        final Meal[] meal = new Meal[1];
        handleNotFound(() -> {
            meal[0] = mealService.get(id, SecurityUtil.authUserId());
        });
        return meal[0];
    }

    private void assignRequestParams(Meal meal, HttpServletRequest request) {
        meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime")));
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));
    }

    private void handleNotFound(Runnable action) {
        try {
            action.run();
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
