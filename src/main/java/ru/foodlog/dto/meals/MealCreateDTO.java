package ru.foodlog.dto.meals;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
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

    @JsonProperty("meal_time")
    private String mealTime;

    @NotNull
    @JsonProperty("meal_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate mealDate;
}
