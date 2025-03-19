package ru.foodlog.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.foodlog.dto.users.UserCreateDTO;

@RequiredArgsConstructor
public class SecurityUtils {

    private final PasswordEncoder passwordEncoder;

    public void encryptPassword(UserCreateDTO userCreateDTO) {
        var password = userCreateDTO.getPassword();
        userCreateDTO.setPassword(passwordEncoder.encode(password));
    }
}
