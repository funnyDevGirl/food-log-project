package ru.foodlog.dto.meals;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class MealDTO {

    private Long id;

    @JsonProperty("user_id")
    private long userId;

    private Set<Long> dishIds;

    private String mealTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String createdAt;
}
