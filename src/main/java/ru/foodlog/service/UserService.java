package ru.foodlog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.foodlog.exception.UserNotFoundException;
import ru.foodlog.mapper.UserMapper;
import ru.foodlog.dto.users.UserCreateDTO;
import ru.foodlog.dto.users.UserDTO;
import ru.foodlog.model.User;
import ru.foodlog.repository.UserRepository;
import ru.foodlog.utils.SecurityUtils;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityUtils securityUtils;
    private final BMRService bmrService;

    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        securityUtils.encryptPassword(userCreateDTO);

        User user = userMapper.toUser(userCreateDTO);
        User savedUser =  userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    public Double getDailyCalories(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
            new UserNotFoundException(format("User with id '%s' not found", id)));

        return bmrService.calculateDailyCalories(user);
    }
}
