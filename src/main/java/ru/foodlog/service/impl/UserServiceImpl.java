package ru.foodlog.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.foodlog.exception.UserNotFoundException;
import ru.foodlog.mapper.UserMapper;
import ru.foodlog.dto.users.UserCreateDTO;
import ru.foodlog.dto.users.UserDTO;
import ru.foodlog.model.User;
import ru.foodlog.repository.UserRepository;
import ru.foodlog.service.BMRService;
import ru.foodlog.service.UserService;
import ru.foodlog.utils.SecurityUtils;
import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityUtils securityUtils;
    private final BMRService bmrService;

    @Override
    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        securityUtils.encryptPassword(userCreateDTO);

        User user = userMapper.toUser(userCreateDTO);
        User savedUser = userRepository.save(user);

        log.debug("User with id '{}' successfully created", savedUser.getId());

        return userMapper.toUserDto(savedUser);
    }

    @Override
    public Double getDailyCalories(Long id) {
        User user = userRepository.findByIdWithEagerUpload(id).orElseThrow(() ->
            new UserNotFoundException(format("User with id '%s' not found", id)));

        return bmrService.calculateDailyCalories(user);
    }

    @Override
    public UserDTO getUser(Long id) {
        User user = userRepository.findByIdWithEagerUpload(id)
                .orElseThrow(() -> new UserNotFoundException(format("User with id: '%s' not found", id)));

        return userMapper.toUserDto(user);
    }
}
