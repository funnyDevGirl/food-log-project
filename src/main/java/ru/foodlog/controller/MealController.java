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
import ru.foodlog.dto.meals.MealCreateDTO;
import ru.foodlog.dto.meals.MealDTO;
import ru.foodlog.service.MealService;
import java.util.List;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MealDTO createMeal(@Valid @RequestBody MealCreateDTO mealCreateDTO) {
        return mealService.createMeal(mealCreateDTO);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MealDTO getById(@PathVariable("id") Long id) {
        return mealService.getMeal(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MealDTO> getAll() {
        return mealService.getAll();
    }
}
