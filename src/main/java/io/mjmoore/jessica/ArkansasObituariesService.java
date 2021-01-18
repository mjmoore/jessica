package io.mjmoore.jessica;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    private final static Predicate<String> regex = Pattern.compile("Jesse.*Mellor").asPredicate();

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL d, yyyy");

    private final static String url = "https://www.arkansasonline.com/obituaries/";

    public List<Person> fetch() {

        final List<Person> people = getDocument().map(doc -> {
            final List<String> names = doc.select("td.deceased-name")
                    .stream()
                    .map(element -> element.children()
                            .stream()
                            .findFirst()
                            .orElseThrow()
                            .ownText())
                    .collect(Collectors.toList());

            final List<Integer> ages = doc.select("td.deceased-age")
                    .stream()
                    .map(Element::ownText)
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());

            final List<LocalDate> deathDate = doc.select("td.deceased-date")
                    .stream()
                    .map(Element::ownText)
                    .map(string -> LocalDate.parse(string, formatter))
                    .collect(Collectors.toList());

            final List<String> locations = doc.select("td.deceased-location")
                    .stream()
                    .map(Element::ownText)
                    .collect(Collectors.toList());

            return IntStream.range(0, names.size())
                    .mapToObj(i -> Person.builder()
                            .age(ages.get(i))
                            .name(names.get(i))
                            .dateOfDeath(deathDate.get(i))
                            .location(locations.get(i))
                            .build())
                    .collect(Collectors.toList());
        }).orElse(Collections.emptyList());

        people.sort(Comparator.comparing(Person::getDateOfDeath, LocalDate::compareTo));
        return people;
    }

    public boolean hasJesse(final List<Person> people) {
        return people.stream()
                .map(Person::getName)
                .noneMatch(regex);
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
