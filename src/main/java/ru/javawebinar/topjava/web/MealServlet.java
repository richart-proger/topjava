package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDaoImpl;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private static final String CREATE_OR_UPDATE = "/modifyMeal.jsp";
    private static final String LIST_MEALS = "/meals.jsp";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static MealDaoImpl mealDao = new MealDaoImpl();
    private static List<MealTo> meals;

    private static List<MealTo> getMealToList() {
        return MealsUtil.filteredByStreams(MealDaoImpl.getMealList(), LocalTime.MIN, LocalTime.MAX, MealDaoImpl.getCaloriesPerDay());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //здесь происходит переход с addMeal на Meals
        log.debug("redirect to CRUD meal = doPost");

        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String dt = request.getParameter("dateTime").replace("T", " ");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime dateTime = LocalDateTime.parse(dt, formatter);

        if (id == null || id.isEmpty()) {
            log.debug("redirect to CRUD = doPost / create");

            mealDao.createMeal(dateTime, description, calories);
        } else {
            log.debug("redirect to CRUD = doPost !!! / update");

            int mealId = Integer.parseInt(id);
            System.out.println("id = " + id);

            mealDao.updateMealById(mealId, dateTime, description, calories);
        }
        meals = getMealToList();
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEALS);
        request.setAttribute("meals", meals);
        view.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //здесь происходит переход с Meals на addMeal
        log.debug("redirect to CRUD meal = doGet");

        String forward = "";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")) {
            log.debug("redirect to CRUD = doGet / delete");

            int id = Integer.parseInt(request.getParameter("id"));
            System.out.println("id = " + id);
            mealDao.deleteMealById(id);
            forward = LIST_MEALS;
            meals = getMealToList();
            request.setAttribute("meals", meals);

        } else if (action.equalsIgnoreCase("update")) {
            log.debug("redirect to CRUD = doGet !!! / update");

////            forward = LIST_MEALS;
            forward = CREATE_OR_UPDATE;

            int id = Integer.parseInt(request.getParameter("id"));
            System.out.println("id = " + id);

//            Meal meal = MealDaoImpl.getMeal(id);
//            request.setAttribute("list", Collections.singletonList(meal));
            request.setAttribute("id", id);

        } else if (action.equalsIgnoreCase("mealList")) {
            log.debug("redirect to CRUD = doGet / meal list");

            forward = LIST_MEALS;
            meals = getMealToList();
            request.setAttribute("meals", meals);
        } else {
//            int id = Integer.parseInt(request.getParameter("id"));
//            request.setAttribute("id", id);
            forward = CREATE_OR_UPDATE;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }
}