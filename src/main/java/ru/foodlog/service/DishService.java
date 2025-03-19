package ru.foodlog.service;

import ru.foodlog.dto.dishes.DishCreateDTO;
import ru.foodlog.dto.dishes.DishDTO;

public interface DishService {

    DishDTO createDish(DishCreateDTO dishCreateDTO);

    DishDTO getDish(Long id);
}
