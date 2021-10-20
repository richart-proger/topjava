package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

public abstract class AbstractMealController {
    private static final Logger log = LoggerFactory.getLogger(AbstractMealController.class);

    @Autowired
    private MealService service;

    // was save
    public Meal create(Meal meal, Integer userId) {
        log.info("create {} with userId={}", meal, userId);
        checkNew(meal);
        return service.create(meal, userId);
    }

    public void update(Meal meal, Integer userId) {
        log.info("update {} with userId={}", meal, userId);
        assureIdConsistent(meal, meal.getId());
        service.update(meal, userId);
    }

    public void delete(int id, Integer userId) {
        log.info("delete {} with userId={}", id, userId);
        service.delete(id, userId);
    }

    public Meal get(int id, Integer userId) {
        log.info("get {} with userId={}", id, userId);
        return service.get(id, userId);
    }

    public List<Meal> getAll(Integer userId, LocalDate startTime, LocalDate endTime) {
        log.info("getAll with userId={}", userId);
        return service.getAll(userId, startTime, endTime);
    }
}
