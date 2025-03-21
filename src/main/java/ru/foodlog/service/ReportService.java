package ru.foodlog.service;

import ru.foodlog.dto.DailyReport;
import ru.foodlog.dto.meals.MealDTO;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    DailyReport getDailyReport(Long userId, LocalDate date);

    boolean isUnderCaloricLimit(Long userId, LocalDate date);

    List<MealDTO> getAllByDateAndUserId(LocalDate date, Long userId);
}
