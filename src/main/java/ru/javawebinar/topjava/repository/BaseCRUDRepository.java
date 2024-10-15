package ru.javawebinar.topjava.repository;

import java.util.List;

public interface BaseCRUDRepository<T> {
    T save(T record);

    boolean delete(int id);

    T get(int id);

    List<T> getAll();
}
