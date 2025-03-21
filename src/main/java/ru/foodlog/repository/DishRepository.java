package ru.foodlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.foodlog.model.Dish;
import java.util.Optional;
import java.util.Set;

public interface DishRepository extends JpaRepository<Dish, Long> {

    @Query("SELECT d FROM Dish d WHERE d.id IN :dishIds")
    Set<Dish> findByIdIn(@Param("dishIds") Set<Long> dishIds);

    @Query("SELECT d FROM Dish d LEFT JOIN FETCH d.meals WHERE d.name = :name")
    Optional<Dish> findByNameWithEagerUpload(@Param("name") String name);

    @Query("SELECT d FROM Dish d LEFT JOIN FETCH d.meals WHERE d.id = :id")
    Optional<Dish> findByIdWithEagerUpload(@Param("id") Long id);
}
