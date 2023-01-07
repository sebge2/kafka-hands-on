package be.sgerard.kafka.utils;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Bunch of utility functions for time.
 */
public class TimeUtils {

    private TimeUtils() {
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

    /**
     * Takes the latest timestamp.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static Instant takeLatest(Instant... instants) {
        return Stream.of(instants)
                .max(Instant::compareTo)
                .get();
    }
}
