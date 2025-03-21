package ru.foodlog.service;

import ru.foodlog.dto.meals.MealCreateDTO;
import ru.foodlog.dto.meals.MealDTO;

public interface MealService {

    MealDTO createMeal(MealCreateDTO mealCreateDTO);

    MealDTO getMeal(Long id);

    double getTotalCalories(Long id);
}
