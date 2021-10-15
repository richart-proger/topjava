package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

public interface MealRepository {
    Meal createMeal(LocalDateTime dateTime, String description, int calories);

    Meal updateMealById(int id, LocalDateTime dateTime, String description, int calories);

    void deleteMealById(int id);
}
