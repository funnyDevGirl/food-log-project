package ru.foodlog.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.foodlog.dto.meals.MealCreateDTO;
import ru.foodlog.dto.meals.MealDTO;
import ru.foodlog.exception.MealNotFoundException;
import ru.foodlog.mapper.MealMapper;
import ru.foodlog.model.Dish;
import ru.foodlog.model.Meal;
import ru.foodlog.repository.MealRepository;
import ru.foodlog.service.MealService;
import java.util.Set;
import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    @Override
    public MealDTO createMeal(MealCreateDTO mealCreateDTO) {
        Meal meal = mealMapper.toMeal(mealCreateDTO);
        Meal savedMeal = mealRepository.save(meal);

        log.debug("Meal with id '{}' successfully created", savedMeal.getId());

        return mealMapper.toDto(savedMeal);
    }

    @Override
    public MealDTO getMeal(Long id) {
        Meal meal = mealRepository.findByIdWithEagerUpload(id).orElseThrow(
                () -> new MealNotFoundException(format("Meal with id '%s' not found", id)));

        return mealMapper.toDto(meal);
    }

    @Override
    public double getTotalCalories(Long id) {
        Meal meal = mealRepository.findByIdWithEagerUpload(id).orElseThrow(
                () -> new MealNotFoundException(format("Meal with id '%s' not found", id)));

        Set<Dish> dishes = meal.getDishes();

        return dishes.stream()
                .mapToDouble(Dish::getCaloriesPerServing)
                .sum();
    }
}
