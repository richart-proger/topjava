package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
private static Integer authUserId;

    public static int authUserId() {
        return SecurityUtil.getAuthUserId();
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }

    public static void setAuthUserId(Integer authUserId) {
        SecurityUtil.authUserId = authUserId;
    }

    public static Integer getAuthUserId() {
        return authUserId;
    }
}