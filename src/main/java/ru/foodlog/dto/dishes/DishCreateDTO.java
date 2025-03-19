package ru.foodlog.dto.dishes;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DishCreateDTO {

    @NotBlank
    private String name;

    @Min(0)
    private double caloriesPerServing;

    @Min(0)
    private double protein;

    @Min(0)
    private double fat;

    @Min(0)
    private double carbohydrates;
}
