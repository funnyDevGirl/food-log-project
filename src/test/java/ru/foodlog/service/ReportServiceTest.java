package ru.foodlog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.foodlog.controller.ReportController;
import ru.foodlog.dto.DailyReport;
import ru.foodlog.dto.meals.MealDTO;
import ru.foodlog.dto.users.UserCreateDTO;
import ru.foodlog.mapper.UserMapper;
import ru.foodlog.model.Dish;
import ru.foodlog.model.Meal;
import ru.foodlog.model.User;
import ru.foodlog.repository.UserRepository;
import ru.foodlog.service.impl.MealServiceImpl;
import ru.foodlog.util.DataFactory;
import ru.foodlog.utils.SecurityUtils;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ReportServiceTest {

    private static final String URI = "/api/reports";

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private MealServiceImpl mealService;

    @Mock
    private ReportService reportService;

    @Mock
    private BMRService bmrService;

    @InjectMocks
    private ReportController reportController; // Используем контроллер

    private final DataFactory dataFactory = new DataFactory();

    private User user;
    private LocalDate date;
    private Meal testMeal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();

        date = LocalDate.parse("2025-03-20");

        // Arrange
        UserCreateDTO userCreateDTO = dataFactory.getTestUserCreateDTO();
        securityUtils.encryptPassword(userCreateDTO);
        String encryptedPassword = userCreateDTO.getPassword();
        user = dataFactory.getTestUser(userCreateDTO, encryptedPassword, LocalDate.now());

        when(userRepository.findByIdWithEagerUpload(user.getId())).thenReturn(Optional.of(user));
    }

    @Test
    public void testGetDailyReportSuccess() throws Exception {
        // Arrange
        Long userId = 1L;
        LocalDate date = LocalDate.of(2025, 3, 20);
        DailyReport dailyReport = new DailyReport(userId, 3, 720.0);

        given(reportService.getDailyReport(userId, date)).willReturn(dailyReport);

        // Act & Assert
        mockMvc.perform(get("/api/reports/daily-report/{userId}/{date}", userId, date)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id").value(userId))
                .andExpect(jsonPath("$.number_of_meals").value(3))
                .andExpect(jsonPath("$.calories").value(720.0));
    }

    @Test
    public void testIsUnderCaloricLimitWhenUserIsUnderLimitShouldReturnTrue() throws Exception {
        // Arrange
        when(reportService.isUnderCaloricLimit(any(Long.class), any(LocalDate.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get(URI + "/caloric-check/{userId}/{date}", user.getId(), date)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testGetMealHistory() throws Exception {
        LocalDate createdAt = LocalDate.now();

        // Arrange
        Set<Dish> dishes = dataFactory.getTestDishes(createdAt);
        Set<Long> dishIds = dataFactory.getDishIds(dishes);

        List<MealDTO> mealHistory = List.of(
                dataFactory.getTestMealDTO(1L, user, dishIds,
                        "For lunch", "2025-03-20", "2025-03-20"));

        when(reportService.getAllByDateAndUserId(date, user.getId())).thenReturn(mealHistory);

        String expectedJson = "[{"
                + "\"id\": 1,"
                + "\"user_id\": 1,"
                + "\"dishIds\": [1, 2],"
                + "\"mealTime\": \"For lunch\","
                + "\"mealDate\": \"2025-03-20\","
                + "\"createdAt\": \"2025-03-20\""
                + "}]";

        // Act & Assert
        mockMvc.perform(get(URI + "/meal-history/{userId}/{date}", user.getId(), date)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(expectedJson));
    }
}
