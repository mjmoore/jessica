package io.mjmoore.jessica;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.function.Function;

public interface Parse<T> extends Function<String, Optional<T>> {

    Parse<Integer> Int = s -> {
        try {
            return Optional.of(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    };

    Parse<LocalDate> Date = new Parse<>() {

        private final DateTimeFormatter format = DateTimeFormatter.ofPattern("LLLL d, yyyy");

        @Override
        public Optional<LocalDate> apply(final String s) {
            try {
                return Optional.of(LocalDate.parse(s, format));
            } catch (DateTimeParseException e) {
                return Optional.empty();
            }
        }
    };
}
