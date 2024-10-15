package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.Comparator;
import java.util.List;

@Service
public class MealService {
    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public List<Meal> getAll() {
        return repository.getAll();
    }

    public List<Meal> getAllForUser(int userId) {
        return repository.getAll(
                m -> m.getUserId() == userId,
                Comparator.comparing(Meal::getDateTime).reversed()
        );
    }

    public Meal get(int id) {
        return repository.get(id);
    }

    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    public void update(Meal meal) {
        repository.save(meal);
    }

    public void delete(Meal meal) {
        repository.delete(meal.getId());
    }
}