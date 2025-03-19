package ru.foodlog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.foodlog.dto.dishes.DishCreateDTO;
import ru.foodlog.dto.dishes.DishDTO;
import ru.foodlog.service.DishService;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DishDTO createDish(@Valid @RequestBody DishCreateDTO dishCreateDTO) {
        return dishService.createDish(dishCreateDTO);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DishDTO getById(@PathVariable("id") Long id) {
        return dishService.getDish(id);
    }
}
