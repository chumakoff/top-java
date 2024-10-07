package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    private static final Map<String, DateTimeFormatter> dateTimeFormatters = new HashMap<String, DateTimeFormatter>() {{
        put("full", DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm"));
    }};

    public static String formatDateTime(LocalDateTime datetime, String format) {
        return datetime.format(dateTimeFormatters.get(format));
    }
}
