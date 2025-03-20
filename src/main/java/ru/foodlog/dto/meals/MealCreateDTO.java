package ru.foodlog.dto.meals;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class MealCreateDTO {

    @NotNull
    @JsonProperty("user_id")
    private long userId;

    @NotNull
    private Set<Long> dishIds;

    private String mealTime;
}
