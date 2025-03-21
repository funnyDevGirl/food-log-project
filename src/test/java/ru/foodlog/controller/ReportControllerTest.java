package ru.foodlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.foodlog.container.BaseContext;
import ru.foodlog.dto.dishes.DishCreateDTO;
import ru.foodlog.dto.meals.MealCreateDTO;
import ru.foodlog.dto.users.UserCreateDTO;
import ru.foodlog.mapper.DishMapper;
import ru.foodlog.mapper.MealMapper;
import ru.foodlog.mapper.UserMapper;
import ru.foodlog.model.Dish;
import ru.foodlog.model.Meal;
import ru.foodlog.model.User;
import ru.foodlog.repository.DishRepository;
import ru.foodlog.repository.MealRepository;
import ru.foodlog.repository.UserRepository;
import ru.foodlog.service.impl.MealServiceImpl;
import ru.foodlog.util.DataFactory;
import ru.foodlog.utils.SecurityUtils;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@AutoConfigureMockMvc
public class ReportControllerTest extends BaseContext  {

    private static final String URI = "/api/reports";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private MealServiceImpl mealService;

    @Autowired
    private MealMapper mealMapper;

    private final DataFactory dataFactory = new DataFactory();

    private User savedUser;

    private Meal testMeal;

    private Meal testAnotherMeal;

    private LocalDate date;

    @BeforeEach
    public void setUp() {
        UserCreateDTO userCreateDTO = dataFactory.getTestUserCreateDTO();

        securityUtils.encryptPassword(userCreateDTO);
        User testUser = userMapper.toUser(userCreateDTO);
        savedUser = userRepository.save(testUser);

        date = LocalDate.parse("2025-03-20");

        Set<DishCreateDTO> dishCreateDTOs = dataFactory.getTestDishCreateDTOs();

                Set<Long> dishes = dishCreateDTOs.stream()
                .map(dishMapper::toDish)
                .map(dishRepository::save)
                .map(Dish::getId)
                .collect(Collectors.toSet());

        MealCreateDTO mealCreateDTO = dataFactory.getTestMealCreateDTO(savedUser, dishes, "For lunch", date);
        testMeal = mealMapper.toMeal(mealCreateDTO);

        Meal savedMeal = mealRepository.save(testMeal);

        for (long dishId : dishes) {
            Dish dish = dishRepository.findByIdWithEagerUpload(dishId).orElseThrow();
            dish.addMeal(savedMeal);
            dishRepository.save(dish);
        }

        Dish anotherDish = dishRepository.findByNameWithEagerUpload("Caesar with chicken").orElseThrow();

        MealCreateDTO anotherMealCreateDTO = dataFactory.getTestMealCreateDTO(savedUser,
                Set.of(anotherDish.getId()), "For branch", date);
        testAnotherMeal = mealMapper.toMeal(anotherMealCreateDTO);

        Meal savedAnotherMeal = mealRepository.save(testAnotherMeal);

        anotherDish.addMeal(savedAnotherMeal);
        dishRepository.save(anotherDish);

        Meal m1 = mealRepository.findByIdWithEagerUpload(testMeal.getId()).orElseThrow();
        Meal m2 = mealRepository.findByIdWithEagerUpload(testAnotherMeal.getId()).orElseThrow();

        savedUser.addMeal(m1);
        savedUser.addMeal(m2);
        userRepository.save(savedUser);
    }

    @AfterEach
    public void clean() {
        mealRepository.deleteAll();
        dishRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testGetDailyReport() throws Exception {
        double expectedTotalCalories =
                mealService.getTotalCalories(testMeal.getId()) + mealService.getTotalCalories(testAnotherMeal.getId());

        mockMvc.perform(get(URI + "/daily-report/{userId}/{date}", savedUser.getId(), date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("user_id").value(savedUser.getId()))
                .andExpect(jsonPath("number_of_meals").value(2))
                .andExpect(jsonPath("calories").value(expectedTotalCalories));
    }

    @Test
    public void testIsUnderCaloricLimit() throws Exception {
        var result = mockMvc.perform(get(URI + "/caloric-check/{userId}/{date}", savedUser.getId(), date))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertEquals("true", body);
    }

    @Test
    public void testMealHistoryByDateAndUserId() throws Exception {
        var result = mockMvc.perform(get(URI + "/meal-history/{userId}/{date}", savedUser.getId(), date))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isArray();
    }
}
