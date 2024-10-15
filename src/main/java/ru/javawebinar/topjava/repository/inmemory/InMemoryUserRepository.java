package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;

@Repository
public class InMemoryUserRepository extends BaseInMemoryRepository<User> implements UserRepository {
    {
        Arrays.asList(
                new User("Regular User", "user@user.user", Role.USER),
                new User("Admin User", "admin@admin.admin", Role.ADMIN)
        ).forEach(this::save);
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return dataSource.values().stream().filter(r -> r.getEmail().equals(email)).findFirst().orElse(null);
    }

    @Override
    protected Comparator<User> defaultSortBy() {
        return Comparator.comparing(User::getId);
    }
}
