package io.mjmoore.jessica;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class Controller {

    private final ArkansasObituariesService service;

    public Controller(final ArkansasObituariesService service) {
        this.service = service;
    }


    @GetMapping
    public ModelAndView get(final ModelAndView modelAndView) {
        final List<Person> people = service.fetch();

        modelAndView.addObject("people", people);
        modelAndView.addObject("jesseIsOk", service.hasJesse(people));
        modelAndView.setViewName("index");

        return modelAndView;
    }
}
