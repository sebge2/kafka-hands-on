package be.sgerard.kafka.utils;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Bunch of utility functions for timestamps.
 */
public class TimestampUtils {

    private TimestampUtils() {
    }

    /**
     * Converts the specified string (can be <tt>null</tt>, or empty) to an {@link Instant}.
     */
    public static Instant toInstant(String value) {
        return Optional.ofNullable(value)
                .filter(v -> !v.isEmpty())
                .map(Instant::parse)
                .orElse(null);
    }

    /**
     * Returns the current date plus a certain number of years (can be negative).
     */
    public static Instant nowPlusYear(int year) {
        return ZonedDateTime.now().plusYears(year).toInstant();
    }
}
