package ru.foodlog.dto.dishes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishDTO {

    private Long id;

    private String name;

    private double caloriesPerServing;

    private double protein;

    private double fat;

    private double carbohydrates;
}
