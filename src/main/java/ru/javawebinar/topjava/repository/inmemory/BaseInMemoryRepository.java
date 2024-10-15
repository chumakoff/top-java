package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.repository.BaseCRUDRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class BaseInMemoryRepository<T extends AbstractBaseEntity> implements BaseCRUDRepository<T> {
    private final AtomicInteger counter = new AtomicInteger(0);
    protected final Map<Integer, T> dataSource = new ConcurrentHashMap<>();
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public T get(int id) {
        log.info("get {}", id);

        return dataSource.get(id);
    }

    @Override
    public T save(T record) {
        log.info("save {}", record);

        if (record.isNew()) {
            record.setId(counter.incrementAndGet());
            dataSource.put(record.getId(), record);
            return record;
        }
        // handle case: update, but not present in storage
        return dataSource.computeIfPresent(record.getId(), (id, oldRecord) -> record);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);

        return dataSource.remove(id) != null;
    }

    public List<T> getAll() {
        return getAll(null, defaultSortBy());
    }

    public List<T> getAll(Predicate<T> filterBy) {
        return getAll(filterBy, defaultSortBy());
    }

    public List<T> getAll(Predicate<T> filterBy, Comparator<T> sortBy) {
        log.info("getAll");

        List<T> result = new ArrayList<>(dataSource.values());

        if (filterBy != null) {
            result = result.stream().filter(filterBy).collect(Collectors.toList());
        }

        sortBy = sortBy == null ? defaultSortBy() : sortBy;
        if (sortBy != null) result.sort(sortBy);

        return result;
    }

    protected Comparator<T> defaultSortBy() {
        return Comparator.comparing(T::getId);
    }
}
