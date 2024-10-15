package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Repository
public class InMemoryUserRepository extends BaseInMemoryRepository<User> implements UserRepository {
    {
        Arrays.asList(
                new User("Regular User", "user@user.user", Role.USER),
                new User("Admin User", "admin@admin.admin", Role.ADMIN)
        ).forEach(this::save);
    }

    public List<User> getAll(Comparator<User> sortBy) {
        return getAll(null, defaultSortBy());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return dataSource.values().stream().filter(r -> r.getEmail().equals(email)).findFirst().orElse(null);
    }
}
