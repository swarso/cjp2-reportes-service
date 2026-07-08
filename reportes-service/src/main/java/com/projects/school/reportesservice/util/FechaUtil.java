package com.projects.school.reportesservice.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FechaUtil {

    private static final DateTimeFormatter FECHA_FORMATTER =
            DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-MX"));

    private FechaUtil() {
    }

    public static String hoyLarga() {
        return LocalDate.now().format(FECHA_FORMATTER);
    }
}
