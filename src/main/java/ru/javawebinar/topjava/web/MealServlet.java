package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealRepositoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private static final String CREATE_OR_UPDATE = "/modifyMeal.jsp";
    private static final String LIST_MEALS = "/meals.jsp";
//    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static MealRepositoryImpl mealDao = new MealRepositoryImpl();
    private static List<MealTo> meals;

    private static List<MealTo> getMealToList() {
        return MealsUtil.filteredByStreams(MealRepositoryImpl.getMealList(), LocalTime.MIN, LocalTime.MAX, MealRepositoryImpl.getCaloriesPerDay());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to CRUD meal = doPost");

        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        LocalDateTime dateTime = TimeUtil.getFormatedDate(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        int mealId = Integer.parseInt(id);

        if (MealRepositoryImpl.getMeal(mealId) == null) {
            Meal meal = mealDao.createMeal(dateTime, description, calories);
            log.debug("\tdoPost / create ", meal);
        } else {
            Meal meal = mealDao.updateMealById(mealId, dateTime, description, calories);
            log.debug("\tdoPost / update", meal);
        }
        meals = getMealToList();
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEALS);
        request.setAttribute("meals", meals);
        view.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to CRUD meal = doGet");

        String forward = "";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")) {
            int id = Integer.parseInt(request.getParameter("id"));
            mealDao.deleteMealById(id);
            forward = LIST_MEALS;
            meals = getMealToList();
            request.setAttribute("meals", meals);
            log.debug("\tdoGet / delete ", id);
        } else if (action.equalsIgnoreCase("mealList")) {

            forward = LIST_MEALS;
            meals = getMealToList();
            request.setAttribute("meals", meals);
            log.debug("\tdoGet / meal list");
        } else {
            Meal meal = "create".equalsIgnoreCase(action) ?
                    new Meal(
                            LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                            "", 500) :
                    MealRepositoryImpl.getMeal(Integer.parseInt(request.getParameter("id")));

            request.setAttribute("meal", meal);
            forward = CREATE_OR_UPDATE;
            log.debug("\tdoGet / " + ("create".equalsIgnoreCase(action) ? "create" : "update"));
        }
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }
}