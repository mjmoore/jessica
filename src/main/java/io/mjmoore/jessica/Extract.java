package io.mjmoore.jessica;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@FunctionalInterface
interface Extract<T> extends Function<Elements, List<Optional<T>>> {


    default List<Optional<T>> apply(final Elements elements) {
        return from(elements);
    }

    List<Optional<T>> from(final Elements elements);

    Extract<String> Name = elements -> elements.stream()
            .map(element -> element.children()
                    .stream()
                    .findFirst()
                    .map(Element::ownText))
            .collect(Collectors.toList());

    Extract<Integer> Age = elements -> elements.stream()
            .map(Element::ownText)
            .map(Parse.Int)
            .collect(Collectors.toList());

    Extract<LocalDate> Date = elements -> elements.stream()
            .map(Element::ownText)
            .map(Parse.Date)
            .collect(Collectors.toList());

    Extract<String> Location = elements -> elements.stream()
            .map(Element::ownText)
            .map(s -> Optional.ofNullable(s)
                    .filter(Predicate.not(String::isEmpty)))
            .collect(Collectors.toList());;

}