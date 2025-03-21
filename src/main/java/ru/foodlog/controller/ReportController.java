package ru.foodlog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.foodlog.dto.DailyReport;
import ru.foodlog.dto.meals.MealDTO;
import ru.foodlog.service.ReportService;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/daily-report/{userId}/{date}")
    @ResponseStatus(HttpStatus.OK)
    public DailyReport getDailyReport(@PathVariable("userId") Long userId,
                                      @PathVariable("date") LocalDate date) {

        return reportService.getDailyReport(userId, date);
    }

    @GetMapping("/caloric-check/{userId}/{date}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isUnderCaloricLimit(@PathVariable("userId") Long userId,
                                       @PathVariable("date") LocalDate date) {

        return reportService.isUnderCaloricLimit(userId, date);
    }

    @GetMapping("/meal-history/{userId}/{date}")
    @ResponseStatus(HttpStatus.OK)
    public List<MealDTO> getMealHistory(@PathVariable("userId") Long userId,
                                        @PathVariable("date") LocalDate date) {
        return reportService.getAllByDateAndUserId(date, userId);
    }
}
