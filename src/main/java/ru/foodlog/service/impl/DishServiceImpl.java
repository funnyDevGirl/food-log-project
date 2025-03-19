package ru.foodlog.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.foodlog.dto.dishes.DishCreateDTO;
import ru.foodlog.dto.dishes.DishDTO;
import ru.foodlog.exception.DishNotFoundException;
import ru.foodlog.mapper.DishMapper;
import ru.foodlog.model.Dish;
import ru.foodlog.repository.DishRepository;
import ru.foodlog.service.DishService;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishMapper dishMapper;
    private final DishRepository dishRepository;

    @Override
    public DishDTO createDish(DishCreateDTO dishCreateDTO) {
        Dish dish = dishMapper.toDish(dishCreateDTO);
        Dish savedDish = dishRepository.save(dish);

        log.info("Dish with name '{}' successfully created", savedDish.getName());

        return dishMapper.toDishDto(savedDish);
    }

    @Override
    public DishDTO getDish(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new DishNotFoundException(format("Dish with id: '%s' not found", id)));

        return dishMapper.toDishDto(dish);
    }
}
