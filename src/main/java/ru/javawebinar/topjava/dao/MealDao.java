package ru.javawebinar.topjava.dao;

import java.time.LocalDateTime;

public interface MealDao {
    void createMeal(LocalDateTime dateTime, String description, int calories);

    void updateMealById(int id, LocalDateTime dateTime, String description, int calories);

    void deleteMealById(int id);
}
