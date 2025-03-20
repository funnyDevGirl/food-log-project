package ru.foodlog.service;

import ru.foodlog.dto.meals.MealCreateDTO;
import ru.foodlog.dto.meals.MealDTO;
import java.util.List;

public interface MealService {

    MealDTO createMeal(MealCreateDTO mealCreateDTO);

    MealDTO getMeal(Long id);

    List<MealDTO> getAll();
}
