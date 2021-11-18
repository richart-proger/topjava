package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController extends AbstractMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    private static final String MEALS = "meals";
    private static final String REDIRECT_MEALS = "redirect:meals";
    private static final String MEAL_FORM = "mealForm";

    public JspMealController(MealService service) {
        super(service);
    }

    @PostMapping("/meals")
    public String updateOrCreateMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.hasLength(request.getParameter("id"))) {
            update(meal, getId(request));
        } else {
            create(meal);
        }
        return REDIRECT_MEALS;
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    @GetMapping("/delete")
    public String deleteMeal(HttpServletRequest request) {
        int id = getId(request);
        delete(id);
        return REDIRECT_MEALS;
    }

    @GetMapping("/create")
    public String createMeal(Model model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return MEAL_FORM;
    }

    @GetMapping("/update")
    public String updateMeal(Model model, HttpServletRequest request) {
        final Meal meal = get(getId(request));
        model.addAttribute("meal", meal);
        return MEAL_FORM;
    }

    @GetMapping("/filter")
    public String filterMeal(Model model, HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        return MEALS;
    }

    @GetMapping("/meals")
    public String getAllMeals(Model model) {
        model.addAttribute("meals", getAll());
        return MEALS;
    }
}
