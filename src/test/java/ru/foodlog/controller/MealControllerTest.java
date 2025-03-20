package ru.foodlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import ru.foodlog.container.BaseContext;
import ru.foodlog.dto.dishes.DishCreateDTO;
import ru.foodlog.dto.meals.MealCreateDTO;
import ru.foodlog.dto.users.UserCreateDTO;
import ru.foodlog.enums.Gender;
import ru.foodlog.enums.Purpose;
import ru.foodlog.mapper.DishMapper;
import ru.foodlog.mapper.MealMapper;
import ru.foodlog.mapper.UserMapper;
import ru.foodlog.model.Dish;
import ru.foodlog.model.Meal;
import ru.foodlog.model.User;
import ru.foodlog.repository.DishRepository;
import ru.foodlog.repository.MealRepository;
import ru.foodlog.repository.UserRepository;
import ru.foodlog.utils.SecurityUtils;
import java.util.HashSet;
import java.util.Set;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class MealControllerTest extends BaseContext {

    private static final String URI = "/api/meals";

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
    private MealMapper mealMapper;

    private Meal testMeal;

    private MealCreateDTO mealCreateDTO;

    @BeforeEach
    public void setUp() {
        UserCreateDTO userCreateDTO = new UserCreateDTO("Alina Tarasova", "test@mail.ru", "qwerty",
                35, 55, 176, Purpose.MAINTENANCE, Gender.FEMALE);

        securityUtils.encryptPassword(userCreateDTO);
        User testUser = userMapper.toUser(userCreateDTO);
        User savedUser = userRepository.save(testUser);

        Set<DishCreateDTO> dishCreateDTOs = Set.of(
                new DishCreateDTO("Chicken grill fillet", 165, 31, 3.6, 0),
                new DishCreateDTO("Caesar with chicken", 450, 28, 25, 30)
                );
        Set<Long> dishes = new HashSet<>();

        for (DishCreateDTO dto : dishCreateDTOs) {
            Dish dish = dishMapper.toDish(dto);
            Dish savedDish = dishRepository.save(dish);
            dishes.add(savedDish.getId());
        }

        mealCreateDTO = new MealCreateDTO(savedUser.getId(), dishes, "For lunch");

        testMeal = mealMapper.toMeal(mealCreateDTO);
    }

    @AfterEach
    public void clean() {
        mealRepository.deleteAll();
        dishRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateMeal() throws Exception {
        var request = post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mealCreateDTO));

        MockHttpServletResponse response = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        String responseBody = response.getContentAsString();
        Meal createdMeal = objectMapper.readValue(responseBody, Meal.class);

        assertThat(createdMeal.getId()).isNotNull();

        Meal meal = mealRepository.findByIdWithEagerUpload(createdMeal.getId()).orElseThrow();

        assertThat(meal.getUser().getId()).isEqualTo(mealCreateDTO.getUserId());
        assertThat(meal.getMealTime()).isEqualTo(mealCreateDTO.getMealTime());
    }

    @Test
    public void testGetById() throws Exception {
        Meal savedMeal = mealRepository.save(testMeal);

        var request = get(URI + "/{id}", savedMeal.getId());

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("mealTime").isEqualTo(savedMeal.getMealTime()),
                v -> v.node("user_id").isEqualTo(savedMeal.getUser().getId())
        );
    }

    @Test
    public void testGetAll() throws Exception {
        var result = mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isArray();
    }
}
