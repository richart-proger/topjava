package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.service.MealService;

@Controller
public class MealRestController extends AbstractMealController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        super(service);
        this.service = service;
    }
}