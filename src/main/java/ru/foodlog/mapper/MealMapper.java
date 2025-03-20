package ru.foodlog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import ru.foodlog.dto.meals.MealCreateDTO;
import ru.foodlog.dto.meals.MealDTO;
import ru.foodlog.exception.UserNotFoundException;
import ru.foodlog.model.Dish;
import ru.foodlog.model.Meal;
import ru.foodlog.model.User;
import ru.foodlog.repository.DishRepository;
import ru.foodlog.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import static java.lang.String.format;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class MealMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DishRepository dishRepository;

    @Mapping(source = "userId", target = "user", qualifiedByName = "idToUser")
    @Mapping(source = "dishIds", target = "dishes", qualifiedByName = "dishIdsToDishes")
    public abstract Meal toMeal(MealCreateDTO mealCreateDTO);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "dishes", target = "dishIds", qualifiedByName = "dishesToDishIds")
    public abstract MealDTO toDto(Meal meal);

    @Named("idToUser")
    public User idToUser(Long userId) {
        return userRepository.findByIdWithEagerUpload(userId).orElseThrow(
                () -> new UserNotFoundException(format("User with id '%s' not found", userId)));
    }

    @Named("dishIdsToDishes")
    public Set<Dish> dishIdsToDishes(Set<Long> dishIds) {
        return dishIds == null ? new HashSet<>()
                : dishRepository.findByIdIn(dishIds);
    }

    @Named("dishesToDishIds")
    public Set<Long> dishesToDishIds(Set<Dish> dishes) {
        return dishes == null ? new HashSet<>()
                : dishes.stream()
                .map(Dish::getId)
                .collect(Collectors.toSet());
    }
}
