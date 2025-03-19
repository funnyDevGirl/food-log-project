package ru.foodlog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.foodlog.dto.dishes.DishCreateDTO;
import ru.foodlog.dto.dishes.DishDTO;
import ru.foodlog.model.Dish;

/**
 * Интерфейс маппера для преобразования объектов Dish и DishDTO.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DishMapper {

    /**
     * Преобразует DishCreateDTO в сущность Dish.
     *
     * @param dishCreateDTO объект DishCreateDTO для преобразования
     * @return преобразованный объект Dish
     */
    Dish toDish(DishCreateDTO dishCreateDTO);

    /**
     * Преобразует сущность Dish в DishDTO.
     *
     * @param dish объект Dish для преобразования
     * @return преобразованный объект DishDTO
     */
    DishDTO toDishDto(Dish dish);
}
