package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Controller
public class MealRestController extends AbstractMealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public void doGetController(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                delete(request, response);
                break;
            case "create":
            case "update":
                createOrUpdate(request, response, action);
                break;
            case "all":
            default:
                getListOfMeals(request, response);
                break;
        }
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = getId(request);
        log.info("Delete {}", id);
        super.delete(id, SecurityUtil.authUserId());
        response.sendRedirect("meals");
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private void createOrUpdate(HttpServletRequest request, HttpServletResponse response, String action) throws ServletException, IOException {
        final Meal meal = "create".equals(action) ?
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                super.get(getId(request), SecurityUtil.authUserId());
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
    }

    private void getListOfMeals(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("getAll");
        request.setAttribute("meals",
                MealsUtil.getTos(super.getAll(SecurityUtil.authUserId(), LocalTime.MIN, LocalTime.MAX), MealsUtil.DEFAULT_CALORIES_PER_DAY));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    public void doPostController(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String userId = request.getParameter("userId");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        meal.setUserId(userId.isEmpty() ? SecurityUtil.authUserId() : Integer.valueOf(userId));

        if (meal.isNew()) {
            log.info("Create {}", meal);
            super.create(meal, SecurityUtil.authUserId());
        } else {
            log.info("Update {}", meal);
            super.update(meal, SecurityUtil.authUserId());
        }
        response.sendRedirect("meals");
    }
}