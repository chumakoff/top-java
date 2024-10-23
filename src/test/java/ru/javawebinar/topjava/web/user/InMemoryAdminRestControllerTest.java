package ru.javawebinar.topjava.web.user;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@Ignore
public class InMemoryAdminRestControllerTest {
    private static final Logger log = LoggerFactory.getLogger(InMemoryAdminRestControllerTest.class);

    private static final InMemoryUserRepository repository = new InMemoryUserRepository();
    private static final AdminRestController controller = new AdminRestController(new UserService(repository));

    @Before
    public void setup() {
        // re-initialize
        repository.init();
    }

    @Test
    public void delete() {
        controller.delete(USER_ID);
        Assert.assertNull(repository.get(USER_ID));
    }

    @Test
    public void deleteNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }
}