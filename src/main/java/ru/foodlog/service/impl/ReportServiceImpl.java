package ru.foodlog.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.foodlog.dto.DailyReport;
import ru.foodlog.dto.meals.MealDTO;
import ru.foodlog.exception.UserNotFoundException;
import ru.foodlog.mapper.MealMapper;
import ru.foodlog.model.Meal;
import ru.foodlog.model.User;
import ru.foodlog.repository.MealRepository;
import ru.foodlog.repository.UserRepository;
import ru.foodlog.service.MealService;
import ru.foodlog.service.ReportService;
import ru.foodlog.service.UserService;
import java.time.LocalDate;
import java.util.List;
import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final UserService userService;
    private final MealService mealService;
    private final MealMapper mealMapper;

    @Override
    public DailyReport getDailyReport(Long userId, LocalDate date) {

        User user = userRepository.findByIdWithEagerUpload(userId)
                .orElseThrow(() -> new UserNotFoundException(format("User with id: '%s' not found", userId)));

        List<Meal> meals = mealRepository.findAllByMealDateAndUserId(date, user.getId());

        double totalCalories = meals.stream()
                .mapToDouble(meal -> mealService.getTotalCalories(meal.getId())).sum();

        return new DailyReport(userId, meals.size(), totalCalories);
    }

    @Override
    public boolean isUnderCaloricLimit(Long userId, LocalDate date) {

        User user = userRepository.findByIdWithEagerUpload(userId)
                .orElseThrow(() -> new UserNotFoundException(format("User with id: '%s' not found", userId)));

        double totalCalories = getDailyReport(user.getId(), date).calories();

        return totalCalories <= userService.getDailyCalories(userId);
    }

    @Override
    public List<MealDTO> getAllByDateAndUserId(LocalDate date, Long userId) {

        return mealRepository.findAllByMealDateAndUserId(date, userId).stream().map(mealMapper::toDto).toList();
    }
}
