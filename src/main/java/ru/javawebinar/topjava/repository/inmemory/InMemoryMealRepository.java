package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setUserId(userId);
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        if (meal.getUserId() == userId) {
            // handle case: update, but not present in storage
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return null;
    }

    @Override
    public boolean delete(int id, Integer userId) {
        log.info("delete {}", id);
        Meal meal = repository.get(id);
        if (meal != null && meal.getUserId() == userId) {
            return repository.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, Integer userId) {
        log.info("get {}", id);
        return repository.values().stream()
                .filter(meal -> meal.getId() == id)
                .filter(meal -> meal.getUserId() == userId)
                .findAny().orElse(null);

//        Meal meal = repository.get(id);
//        if (meal.getUserId() == userId) {
//            return meal;
//        }
//        return null;
    }

    @Override
    public Collection<Meal> getAll(Integer userId, LocalTime startTime, LocalTime endTime) {
        log.info("getAll");
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .filter(m -> DateTimeUtil.isBetweenHalfOpen(m.getTime(), startTime, endTime))
                .sorted((m1, m2) -> m2.getDate().compareTo(m1.getDate()))
                .collect(Collectors.toList());
    }
}