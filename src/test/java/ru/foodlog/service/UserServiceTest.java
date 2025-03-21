package ru.foodlog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.foodlog.dto.users.UserCreateDTO;
import ru.foodlog.dto.users.UserDTO;
import ru.foodlog.exception.UserNotFoundException;
import ru.foodlog.mapper.UserMapper;
import ru.foodlog.model.User;
import ru.foodlog.repository.UserRepository;
import ru.foodlog.service.impl.UserServiceImpl;
import ru.foodlog.util.DataFactory;
import ru.foodlog.utils.SecurityUtils;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private BMRService bmrService;

    @InjectMocks
    private UserServiceImpl userService;

    private final DataFactory dataFactory = new DataFactory();

    private UserCreateDTO userCreateDTO;

    private User user;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Arrange
        userCreateDTO = dataFactory.getTestUserCreateDTO();

        securityUtils.encryptPassword(userCreateDTO);
        String encryptedPassword = userCreateDTO.getPassword();

        LocalDate createdAt = LocalDate.now();

        user = dataFactory.getTestUser(userCreateDTO, encryptedPassword, createdAt);

        userDTO = dataFactory.getTestUserDTO(createdAt);
    }

    @Test
    public void testGetDailyCaloriesUserFound() {
        Long userId = 1L;
        when(userRepository.findByIdWithEagerUpload(userId)).thenReturn(Optional.of(user));
        when(bmrService.calculateDailyCalories(user)).thenReturn(2500.0);

        Double result = userService.getDailyCalories(userId);

        assertNotNull(result);
        assertEquals(2500.0, result);

        verify(userRepository).findByIdWithEagerUpload(userId);
        verify(bmrService).calculateDailyCalories(user);
    }

    @Test
    public void testGetDailyCaloriesUserNotFound() {
        Long userId = 1L;
        when(userRepository.findByIdWithEagerUpload(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getDailyCalories(userId);
        });

        assertTrue(exception.getMessage().contains("User with id '1' not found"));

        verify(userRepository).findByIdWithEagerUpload(userId);
    }


    @Test
    public void testCreateUser() {
        when(userMapper.toUser(userCreateDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDTO);

        doNothing().when(securityUtils).encryptPassword(any(UserCreateDTO.class));

        UserDTO result = userService.createUser(userCreateDTO);

        assertNotNull(result);
        assertEquals(userDTO, result);

        verify(userMapper).toUser(userCreateDTO);
        verify(userRepository).save(user);
        verify(userMapper).toUserDto(user);
    }
}
