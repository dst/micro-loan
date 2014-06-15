package com.stefanski.loan.rest.util;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_DATE;

/**
 * @author Dariusz Stefanski
 */
public class Formatter {

    public static String formatTime(LocalDateTime time) {
        return time.format(ISO_DATE);
    }
}
