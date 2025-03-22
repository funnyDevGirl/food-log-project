package ru.foodlog.util;

import ru.foodlog.dto.dishes.DishCreateDTO;
import ru.foodlog.dto.meals.MealCreateDTO;
import ru.foodlog.dto.meals.MealDTO;
import ru.foodlog.dto.users.UserCreateDTO;
import ru.foodlog.dto.users.UserDTO;
import ru.foodlog.enums.Gender;
import ru.foodlog.enums.Purpose;
import ru.foodlog.model.Dish;
import ru.foodlog.model.Meal;
import ru.foodlog.model.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

public class DataFactory {

    public UserCreateDTO getTestUserCreateDTO() {
        return new UserCreateDTO("Alina Tarasova", "test@mail.ru", "qwerty",
                35, 55, 176, Purpose.MAINTENANCE, Gender.FEMALE);
    }

    public User getTestUser(UserCreateDTO userCreateDTO, String encryptedPassword, LocalDate createdAt) {

        User user = new User();
        user.setId(1L);
        user.setName("Alina Tarasova");
        user.setEmail("test@mail.ru");
        user.setPassword(encryptedPassword);
        user.setGender(Gender.FEMALE);
        user.setAge(35);
        user.setWeight(55);
        user.setHeight(176);
        user.setPurpose(Purpose.MAINTENANCE);
        user.setCreatedAt(createdAt);

        return user;
    }

    public UserDTO getTestUserDTO(LocalDate createdAt) {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Alina Tarasova");
        userDTO.setEmail("test@mail.ru");
        userDTO.setGender(Gender.FEMALE);
        userDTO.setAge(35);
        userDTO.setWeight(55);
        userDTO.setHeight(176);
        userDTO.setPurpose(Purpose.MAINTENANCE);
        userDTO.setCreatedAt(createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        return userDTO;
    }

    public Set<DishCreateDTO> getTestDishCreateDTOs() {
        DishCreateDTO dto = new DishCreateDTO();
        dto.setName("Chicken grill fillet");
        dto.setCaloriesPerServing(165);
        dto.setProtein(31);
        dto.setFat(3.6);
        dto.setCarbohydrates(0);

        DishCreateDTO anotherDto = new DishCreateDTO();
        anotherDto.setName("Caesar with chicken");
        anotherDto.setCaloriesPerServing(450);
        anotherDto.setProtein(28);
        anotherDto.setFat(25);
        anotherDto.setCarbohydrates(30);

        return Set.of(dto, anotherDto);
    }

    public Set<Dish> getTestDishes(LocalDate createdAt) {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Chicken grill fillet");
        dish.setCaloriesPerServing(165);
        dish.setProtein(31);
        dish.setFat(3.6);
        dish.setCarbohydrates(0);
        dish.setCreatedAt(createdAt);
        dish.setUpdatedAt(null);

        Dish anotherDish = new Dish();
        anotherDish.setId(2L);
        anotherDish.setName("Caesar with chicken");
        anotherDish.setCaloriesPerServing(450);
        anotherDish.setProtein(28);
        anotherDish.setFat(25);
        anotherDish.setCarbohydrates(30);
        anotherDish.setCreatedAt(createdAt);
        anotherDish.setUpdatedAt(null);

        return Set.of(dish, anotherDish);
    }

    public Set<Long> getDishIds(Set<Dish> dishes) {
        return dishes.stream().map(Dish::getId).collect(Collectors.toSet());
    }

    public MealCreateDTO getTestMealCreateDTO(User user, Set<Long> dishIds, String mealTime, LocalDate date) {
        return new MealCreateDTO(user.getId(), dishIds,
                mealTime, date);
    }

    public Meal getTestMeal(Long id, User user, Set<Dish> dishes, String mealTime,
                            LocalDate mealDate, LocalDate createdAt) {
        return new Meal(id, user, mealTime, mealDate, createdAt, null, dishes);
    }

    public MealDTO getTestMealDTO(Long id, User user, Set<Long> dishIds, String mealTime,
                                  String mealDate, String createdAt) {
        MealDTO mealDTO = new MealDTO();
        mealDTO.setId(id);
        mealDTO.setUserId(user.getId());
        mealDTO.setDishIds(dishIds);
        mealDTO.setMealTime(mealTime);
        mealDTO.setMealDate(mealDate);
        mealDTO.setCreatedAt(createdAt);

        return mealDTO;
    }
}
