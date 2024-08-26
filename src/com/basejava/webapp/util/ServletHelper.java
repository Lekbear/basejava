package com.basejava.webapp.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ServletHelper {
    public static boolean validateText(String text) {
        return text != null && !text.trim().isEmpty();
    }

    public static LocalDate parseLocalDate(String date) {
        if (!validateText(date)) {
            return null;
        }
        try {
            return LocalDate.parse(date.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
