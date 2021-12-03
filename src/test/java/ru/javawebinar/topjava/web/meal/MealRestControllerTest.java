package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;

class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = "/rest/profile/meals/";

    @Autowired
    private MealService mealService;

    @Test
    void getMeal() throws Exception {
        perform(get(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void deleteMeal() throws Exception {
        perform(delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        List<Meal> mealList = mealService.getAll(SecurityUtil.authUserId());
        MEAL_MATCHER.assertMatch(mealList, meal7, meal6, meal5, meal4, meal3, meal2);
    }

    @Test
    void getAllMeals() throws Exception {
        List<MealTo> mealToList = MealsUtil.getTos(meals, UserTestData.user.getCaloriesPerDay());

        perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(mealToList));
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = getNew();

        ResultActions action = perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());

        Meal retrieve = MEAL_MATCHER.readFromJson(action);
        newMeal.setId(retrieve.getId());

        MEAL_MATCHER.assertMatch(retrieve, newMeal);
        MEAL_MATCHER.assertMatch(mealService.getAll(SecurityUtil.authUserId()), newMeal, meal7, meal6, meal5, meal4, meal3, meal2, meal1);
    }

    @Test
    void update() throws Exception {
        Meal updated = getUpdated();

        perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(mealService.get(updated.getId(), SecurityUtil.authUserId()), updated);
    }

    @Test
    void getBetween() throws Exception {
        List<MealTo> mealTos = List.of(MealsUtil.createTo(meal6, true), MealsUtil.createTo(meal2, false));

        perform(get(REST_URL + "/between?startDateTime=2020-01-30T12:00:00&endDateTime=2021-04-30T20:00:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MEAL_TO_MATCHER.contentJson(mealTos));
    }

    @Test
    void getFilter() throws Exception {
        List<MealTo> mealTos = List.of(
                MealsUtil.createTo(meal5, true),
                MealsUtil.createTo(meal4, true),
                MealsUtil.createTo(meal1, false));

        perform(get(REST_URL + "/filter?startDate=2020-01-30&endDate=2021-04-30&startTime=00:00&endTime=13:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MEAL_TO_MATCHER.contentJson(mealTos));
    }

    @Test
    void getFilterWithAll() throws Exception {
        List<MealTo> mealTos = List.of(
                MealsUtil.createTo(meal7, true),
                MealsUtil.createTo(meal6, true),
                MealsUtil.createTo(meal5, true),
                MealsUtil.createTo(meal4, true),
                MealsUtil.createTo(meal3, false),
                MealsUtil.createTo(meal2, false),
                MealsUtil.createTo(meal1, false));

        perform(get(REST_URL + "/filter?startDate=&endDate=&startTime=&endTime="))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MEAL_TO_MATCHER.contentJson(mealTos));
    }
}