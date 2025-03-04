package com.mobile.base.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

/**
 * Utility class for date and time operations.
 */
@Slf4j
public class DateUtils {

    public static final String FORMAT_DATE = "dd/MM/yyyy HH:mm:ss";

    private DateUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a LocalDate to Date using system default timezone.
     */
    public static Date convertToDateViaInstant(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    /**
     * Formats a Date object into a string with the default format.
     */
    public static String formatDate(Date date) {
        return formatDate(date, FORMAT_DATE);
    }

    /**
     * Formats a Date object into a string with the given format.
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat fm = new SimpleDateFormat(format);
        return fm.format(date);
    }

    /**
     * Converts a String to Date using the specified format.
     */
    public static Date convertDate(String date, String format) {
        try {
            SimpleDateFormat fm = new SimpleDateFormat(format);
            return fm.parse(date);
        } catch (ParseException e) {
            log.error("Error parsing date: {}", date, e);
        }
        return null;
    }

    /**
     * Converts a String to Date using the default format.
     */
    public static Date convertDate(String date) {
        return convertDate(date, FORMAT_DATE);
    }

    /**
     * Checks if the given date is within the last X minutes.
     */
    public static boolean isInRangeXMinutesAgo(Date date, int minutes) {
        Instant instant = date.toInstant();
        Instant minutesAgo = Instant.now().minus(Duration.ofMinutes(minutes));
        return minutesAgo.isBefore(instant);
    }

    /**
     * Checks if the given date is at least X seconds ago.
     */
    public static boolean isAtLeastXSecondsAgo(Date date, int seconds) {
        Instant instant = date.toInstant();
        Instant secondsAgo = Instant.now().minus(Duration.ofSeconds(seconds));
        return instant.isBefore(secondsAgo);
    }

    /**
     * Returns the start of the given date (00:00:00 UTC).
     */
    public static Date startOfDay(Date date) {
        return Date.from(date.toInstant().atZone(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC).toInstant());
    }

    /**
     * Converts a LocalDate to Date using system default timezone.
     */
    public static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Converts a Date to LocalDate using system default timezone.
     */
    public static LocalDate convertDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Returns the end of the given date (23:59:59.999 UTC).
     */
    public static Date endOfDay(Date date) {
        return Date.from(date.toInstant().atZone(ZoneOffset.UTC).toLocalDate().atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant());
    }

    /**
     * Returns the start of the given date in UTC, adjusting for the specified timezone.
     */
    public static Date startOfDayUTC(Date sourceDate, TimeZone timeZone) throws ParseException {
        return getUtcDate(sourceDate, "00:00:00", timeZone);
    }

    /**
     * Returns the end of the given date in UTC, adjusting for the specified timezone.
     */
    public static Date endOfDayUTC(Date sourceDate, TimeZone timeZone) throws ParseException {
        return getUtcDate(sourceDate, "23:59:59", timeZone);
    }

    /**
     * Helper method to convert a date to UTC start/end time.
     */
    private static Date getUtcDate(Date sourceDate, String time, TimeZone timeZone) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        dateFormat.setTimeZone(timeZone);

        String dateString = new SimpleDateFormat("dd.MM.yyyy").format(sourceDate) + " " + time;
        return dateFormat.parse(dateString);
    }

    /**
     * Returns the current store date in UTC.
     */
    public static Date getCurrentStoreDate(TimeZone timeZone) throws ParseException {
        SimpleDateFormat utcFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        utcFormat.setTimeZone(timeZone);
        return utcFormat.parse(utcFormat.format(new Date()));
    }

    /**
     * Gets the UTC offset of the given timezone in "+HH:MM" format.
     */
    public static String getOffset(TimeZone tz) {
        int offsetInMillis = tz.getOffset(System.currentTimeMillis());
        return String.format("%s%02d:%02d",
                (offsetInMillis >= 0 ? "+" : "-"),
                Math.abs(offsetInMillis / 3600000),
                Math.abs((offsetInMillis / 60000) % 60));
    }

    /**
     * Converts a given date from a specified timezone to UTC.
     */
    public static Date convertToUtc(Date source, TimeZone oldTimeZone) throws ParseException {
        SimpleDateFormat utcFormat = new SimpleDateFormat(FORMAT_DATE);
        SimpleDateFormat oldFormat = new SimpleDateFormat(FORMAT_DATE);
        oldFormat.setTimeZone(oldTimeZone);

        return utcFormat.parse(utcFormat.format(oldFormat.parse(oldFormat.format(source))));
    }
}
