package ru.foodlog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DailyReport(
        @JsonProperty("user_id") long userId,
        @JsonProperty("number_of_meals") int numberOfMeals,
        double calories
) {
}
