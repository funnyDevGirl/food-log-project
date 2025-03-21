package ru.foodlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.foodlog.container.BaseContext;
import ru.foodlog.dto.dishes.DishCreateDTO;
import ru.foodlog.mapper.DishMapper;
import ru.foodlog.model.Dish;
import ru.foodlog.repository.DishRepository;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc
class DishControllerTest extends BaseContext {

    private static final String URI = "/api/dishes";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private DishMapper dishMapper;

    private Dish testDish;

    private DishCreateDTO dishCreateDTO;

    @BeforeEach
    public void setUp() {
        dishCreateDTO = new DishCreateDTO();
        dishCreateDTO.setName("Chicken fillet");
        dishCreateDTO.setCaloriesPerServing(165);
        dishCreateDTO.setProtein(31);
        dishCreateDTO.setFat(3.6);
        dishCreateDTO.setCarbohydrates(0);

        testDish = dishMapper.toDish(dishCreateDTO);
    }

    @AfterEach
    public void clean() {
        dishRepository.deleteAll();
    }

    @Test
    public void testSuccessCreateDish() throws Exception {
        var request = post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dishCreateDTO));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        Dish dish = dishRepository.findByNameWithEagerUpload(testDish.getName()).orElseThrow();

        assertThat(dish).isNotNull();
        assertThat(dish.getName()).isEqualTo(testDish.getName());
        assertThat(dish.getCaloriesPerServing()).isEqualTo(testDish.getCaloriesPerServing());
    }

    @Test
    public void testCreateDishWithNotValidName() throws Exception {
        Dish dish = new Dish();
        dish.setName("");

        var request = post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dish));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetById() throws Exception {
        Dish savedDish = dishRepository.save(testDish);

        var request = get(URI + "/{id}", savedDish.getId());

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(savedDish.getName()),
                v -> v.node("caloriesPerServing").isEqualTo(savedDish.getCaloriesPerServing()),
                v -> v.node("protein").isEqualTo(savedDish.getProtein()),
                v -> v.node("fat").isEqualTo(savedDish.getFat()),
                v -> v.node("carbohydrates").isEqualTo(savedDish.getCarbohydrates())
        );
    }
}
