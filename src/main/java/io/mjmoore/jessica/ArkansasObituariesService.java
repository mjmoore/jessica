package io.mjmoore.jessica;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class ArkansasObituariesService {

    private final static Predicate<String> jessePresent = Pattern.compile("Jesse.*Mellor").asPredicate();

    private final static String url = "https://www.arkansasonline.com/obituaries/";

    public List<Person> fetch() {

        final List<Person> people = getDocument().map(doc -> {

            final List<Optional<String>> names = Extract.Name.from(doc.select("td.deceased-name"));
            final List<Optional<Integer>> ages = Extract.Age.from(doc.select("td.deceased-age"));
            final List<Optional<LocalDate>> dates = Extract.Date.from(doc.select("td.deceased-date"));
            final List<Optional<String>> locations = Extract.Location.from(doc.select("td.deceased-location"));

            // TODO: Find a nicer way of zip all - JOOLs API sucks
            return IntStream.range(0, names.size())
                    .mapToObj(i -> Person.builder()
                            .age(ages.get(i))
                            .name(names.get(i))
                            .dateOfDeath(dates.get(i))
                            .location(locations.get(i))
                            .build())
                    .collect(Collectors.toList());
        }).orElse(Collections.emptyList());

        people.sort(Comparator.comparing(Person::getDateOfDeath,
                Comparator.comparing(date -> date.orElse(LocalDate.EPOCH))));

        people.add(Person.builder().build());
        return people;
    }

    public boolean hasJesse(final List<Person> people) {
        return people.stream()
                .map(Person::getName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .noneMatch(jessePresent);
    }

    private Optional<Document> getDocument() {
        try {
            return Optional.of(Jsoup.connect(url).get());
        } catch (IOException e) {
            log.info("Couldn't connect to remote: {}", url);
        }

        return Optional.empty();
    }

}
