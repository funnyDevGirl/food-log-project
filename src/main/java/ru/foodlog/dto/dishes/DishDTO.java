package ru.foodlog.dto.dishes;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String createdAt;
}
