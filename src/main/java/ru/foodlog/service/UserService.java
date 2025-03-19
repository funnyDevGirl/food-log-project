package ru.foodlog.service;

import ru.foodlog.dto.users.UserCreateDTO;
import ru.foodlog.dto.users.UserDTO;

public interface UserService {

    UserDTO createUser(UserCreateDTO userCreateDTO);

    Double getDailyCalories(Long id);

    UserDTO getUser(Long id);
}
