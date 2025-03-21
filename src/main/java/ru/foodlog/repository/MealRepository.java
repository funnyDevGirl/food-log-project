package ru.foodlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.foodlog.model.Meal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long> {

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.dishes LEFT JOIN FETCH m.user WHERE m.id = :id")
    Optional<Meal> findByIdWithEagerUpload(@Param("id") Long id);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.dishes LEFT JOIN FETCH m.user " +
            "WHERE m.mealDate = :date AND m.user.id = :userId")
    List<Meal> findAllByMealDateAndUserId(@Param("date") LocalDate date, @Param("userId") Long userId);
}
