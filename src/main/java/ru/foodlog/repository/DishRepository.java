package ru.foodlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.foodlog.model.Dish;

import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long> {

    Optional<Dish> findByName(String name);
}
