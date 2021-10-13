package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoImpl implements MealDao {
    private static Map<Integer, Meal> meals = MealStorage.getMeals();

    public static List<Meal> getMealList() {
        return new ArrayList<>(meals.values());
    }

    public static int getCaloriesPerDay() {
        return MealStorage.caloriesPerDay;
    }

    public static AtomicInteger getCounter() {
        return MealStorage.counter;
    }

    public static Meal getMeal(int id) {
        return meals.get(id);
    }

    @Override
    public void createMeal(LocalDateTime dateTime, String description, int calories) {
        Meal meal = new Meal(dateTime, description, calories);
        meals.put(meal.getId(), meal);
    }

    @Override
    public void updateMealById(int id, LocalDateTime dateTime, String description, int calories) {
        Meal meal = new Meal(id, dateTime, description, calories);
        meals.put(id, meal);
    }

    @Override
    public void deleteMealById(int id) {
        meals.remove(id);
    }
}
