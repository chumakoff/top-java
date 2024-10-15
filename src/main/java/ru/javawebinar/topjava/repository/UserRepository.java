package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.User;

import java.util.Comparator;
import java.util.List;

public interface UserRepository extends BaseCRUDRepository<User> {
    // null if not found
    User getByEmail(String email);

    List<User> getAll(Comparator<User> sortBy);
}