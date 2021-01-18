package io.mjmoore.jessica;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class Person {

    private final String name;
    private final LocalDate dateOfDeath;
    private final String location;
    private final Integer age;
}
