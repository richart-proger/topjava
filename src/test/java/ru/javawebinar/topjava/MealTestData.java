package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
//    public static final int ID = 1;
    public static final int NOT_FOUND = 10;


    public static final Meal meal1 = new Meal(1, LocalDateTime.parse("2020-12-19T10:07:04"), "Breakfast", 416);
    public static final Meal meal2 = new Meal(2, LocalDateTime.parse("2021-07-11T06:51:10"), "Breakfast", 2128);
    public static final Meal meal3 = new Meal(3, LocalDateTime.parse("2020-11-12T05:31:33"), "Afternoon snack", 1084);
    public static final Meal meal4 = new Meal(4, LocalDateTime.parse("2021-04-15T07:37:50"), "Afternoon snack", 1907);
    public static final Meal meal5 = new Meal(5, LocalDateTime.parse("2021-03-12T10:28:08"), "Breakfast", 637);
    public static final Meal meal6 = new Meal(6, LocalDateTime.parse("2021-04-27T17:25:54"), "Lunch", 1021);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.parse("1985-12-26T07:30:00"), "Breakfast", 777);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal1);
        updated.setDateTime(LocalDateTime.parse("1985-12-26T07:30:00"));
        updated.setDescription("Снова жрать");
        updated.setCalories(999);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

//    public static void assertMatch(Iterable<Meal> actual, Meal ... expected) {
//        assertMatch(actual, Arrays.asList(expected));
//    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
