package ru.javawebinar.topjava.util;


import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.util.exception.NotFoundException;

public class ValidationUtil {

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFound(object, "id=" + id);
        return object;
    }

    public static <T> T checkNotFound(T object, String msg) {
        if (object == null) {
            throw new NotFoundException("Not found entity with " + msg);
        }
        return object;
    }

    public static void checkIsNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(AbstractBaseEntity entity, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.getId() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }
}