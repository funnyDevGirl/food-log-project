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
import ru.foodlog.dto.users.UserCreateDTO;
import ru.foodlog.enums.Gender;
import ru.foodlog.enums.Purpose;
import ru.foodlog.mapper.UserMapper;
import ru.foodlog.model.Dish;
import ru.foodlog.model.User;
import ru.foodlog.repository.UserRepository;
import ru.foodlog.utils.SecurityUtils;
import java.time.LocalDate;
import java.time.ZoneId;
import static java.lang.String.format;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class UserControllerTest extends BaseContext {

    private static final String URI = "/api/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SecurityUtils securityUtils;

    private User testUser;

    private UserCreateDTO userCreateDTO;

    @BeforeEach
    public void setUp() {
        userCreateDTO = new UserCreateDTO("Alina Tarasova", "test@mail.ru", "qwerty",
                35, 55, 176, Purpose.MAINTENANCE, Gender.FEMALE);

        securityUtils.encryptPassword(userCreateDTO);

        testUser = userMapper.toUser(userCreateDTO);
    }

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
    }

    @Test
    public void testSuccessCreateUserAndCheckCorrectCreatedAt() throws Exception {
        var request = post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        User user = userRepository.findByEmail(testUser.getEmail()).orElseThrow();

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(user.getPassword()).isNotEqualTo(testUser.getPassword());


        assertNotNull(user);
        assertNotNull(user.getCreatedAt());

        LocalDate today = LocalDate.now(ZoneId.of("UTC"));

        assertTrue(user.getCreatedAt().isBefore(today) || user.getCreatedAt().isEqual(today));
    }

    @Test
    public void testCreateWithNotValidEmail() throws Exception {
        User user = new User();
        user.setEmail("invalid_email");

        var request = post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWithNotValidPassword() throws Exception {
        User user = new User();
        user.setPassword("");

        var request = post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetDailyCaloriesUserNotFound() throws Exception {
        Long notFoundUserId = 999L;

        var request = get(URI + "/{id}/daily-calories", notFoundUserId);

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().string(format("{\"code\":404,\"message\":\"User with id '%s' not found\"}",
                        notFoundUserId)));
    }

    @Test
    public void testGetDailyCaloriesSuccess() throws Exception {
        double expectedCalories = 2159.8016000000002;

        User savedUser = userRepository.save(testUser);

        var request = get(URI + "/{id}/daily-calories", savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedUser));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedCalories)));
    }

    @Test
    public void testGetById() throws Exception {
        User savedUser = userRepository.save(testUser);

        var request = get(URI + "/{id}", savedUser.getId());

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(savedUser.getName()),
                v -> v.node("email").isEqualTo(savedUser.getEmail()),
                v -> v.node("age").isEqualTo(savedUser.getAge()),
                v -> v.node("weight").isEqualTo(savedUser.getWeight()),
                v -> v.node("height").isEqualTo(savedUser.getHeight()),
                v -> v.node("purpose").isEqualTo(savedUser.getPurpose()),
                v -> v.node("gender").isEqualTo(savedUser.getGender())
        );
    }
}
