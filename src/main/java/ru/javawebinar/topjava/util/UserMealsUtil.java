package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 400),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 12, 0), "Второй завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 401),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 28, 11, 0), "Завтрак", 2000)
        );

        List<UserMealWithExcess> mealsTo = filteredWithOneStreamOptional(
                meals,
                LocalTime.of(7, 0),
                LocalTime.of(12, 0), 1900
        );
        mealsTo.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mealExcessPerDay = new HashMap<>();

        for (UserMeal user : meals) {
            mealExcessPerDay.put(
                    user.getDateTime().toLocalDate(),
                    mealExcessPerDay.getOrDefault(user.getDateTime().toLocalDate(), 0) + user.getCalories());
        }

        List<UserMealWithExcess> result = new LinkedList<>();

        for (UserMeal user : meals) {
            boolean isTimeCorrect = TimeUtil.isBetweenHalfOpen(user.getDateTime().toLocalTime(), startTime, endTime);

            if (isTimeCorrect) {
                result.add(new UserMealWithExcess(
                        user.getDateTime(),
                        user.getDescription(),
                        user.getCalories(),
                        mealExcessPerDay.get(user.getDateTime().toLocalDate()) > caloriesPerDay
                ));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mealExcessPerDay = meals.stream()
                .collect(Collectors.toMap(k -> k.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));

        Predicate<UserMeal> byLocalTime = u -> TimeUtil.isBetweenHalfOpen(u.getDateTime().toLocalTime(), startTime, endTime);

        Function<UserMeal, UserMealWithExcess> addUserMealWithoutExcess = u -> new UserMealWithExcess(u.getDateTime(), u.getDescription(), u.getCalories(), mealExcessPerDay.get(u.getDateTime().toLocalDate()) > caloriesPerDay);

        List<UserMealWithExcess> result = meals.stream()
                .filter(byLocalTime)
                .map(addUserMealWithoutExcess)
                .collect(Collectors.toList());
        return result;
    }

    public static List<UserMealWithExcess> filteredWithOneCycleOptional(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate, Integer> tempMapMealExcess = new HashMap<>();
        Map<LocalDate, UserMeal> tempMapUserMeal = new HashMap<>();
        Map<LocalDate, UserMealWithExcess> tempMapUserMealWithExcess = new HashMap<>();

        UserMealWithExcess userMealWithExcess = null;
        UserMealWithExcess prevUserMealWithExcess;
        UserMeal prevU = null;

        for (UserMeal u : meals) {
            tempMapMealExcess.put(
                    u.getDateTime().toLocalDate(),
                    tempMapMealExcess.getOrDefault(u.getDateTime().toLocalDate(), 0) + u.getCalories()
            );

            if (tempMapUserMeal.containsKey(u.getDateTime().toLocalDate())) {
                prevU = tempMapUserMeal.get(u.getDateTime().toLocalDate());
            } else {
                tempMapUserMeal.put(u.getDateTime().toLocalDate(), u);
            }

            if (TimeUtil.isBetweenHalfOpen(u.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExcess = getNewUserMealWithExcess(u, tempMapMealExcess, caloriesPerDay);
            } else {
                if (tempMapUserMeal.containsKey(u.getDateTime().toLocalDate())) {
                    userMealWithExcess = getNewUserMealWithExcess(prevU, tempMapMealExcess, caloriesPerDay);
                }
            }
            if (tempMapUserMealWithExcess.containsKey(u.getDateTime().toLocalDate())) {
                prevUserMealWithExcess = tempMapUserMealWithExcess.remove(u.getDateTime().toLocalDate());
                tempMapUserMealWithExcess.put(u.getDateTime().toLocalDate(), userMealWithExcess);
                result.remove(prevUserMealWithExcess);
                result.add(userMealWithExcess);
            } else {
                tempMapUserMealWithExcess.put(u.getDateTime().toLocalDate(), userMealWithExcess);
                result.add(userMealWithExcess);
            }
        }
        return result;
    }

    private static UserMealWithExcess getNewUserMealWithExcess(UserMeal userMeal, Map<LocalDate, Integer> tempMapMealExcess, Integer caloriesPerDay) {
        return new UserMealWithExcess(
                userMeal.getDateTime(),
                userMeal.getDescription(),
                userMeal.getCalories(),
                tempMapMealExcess.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay
        );
    }

    public static List<UserMealWithExcess> filteredWithOneStreamOptional(List<UserMeal> meals, LocalTime
            startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .filter(u -> TimeUtil.isBetweenHalfOpen(u.getDateTime().toLocalTime(), startTime, endTime))
                .map(m -> new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(),
                        meals.stream()
                                .filter(foo -> foo.getDateTime().toLocalDate().equals(m.getDateTime().toLocalDate()))
                                .mapToInt(UserMeal::getCalories)
                                .reduce(0, Integer::sum) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
