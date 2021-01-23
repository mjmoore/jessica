package io.mjmoore.jessica;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Builder
public class Person {

    @Builder.Default
    private final Optional<String> name = Optional.empty();

    @Builder.Default
    private final Optional<LocalDate> dateOfDeath = Optional.empty();

    @Builder.Default
    private final Optional<String> location = Optional.empty();

    @Builder.Default
    private final Optional<Integer> age = Optional.empty();
}
