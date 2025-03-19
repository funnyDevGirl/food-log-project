package ru.foodlog.dto.users;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.foodlog.enums.Gender;
import ru.foodlog.enums.Purpose;

@Getter
@Setter
public class UserCreateDTO {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 3)
    private String password;

    @NotNull
    @Min(0)
    @Max(100)
    private int age;

    @NotNull
    @Min(0)
    private double weight;

    @NotNull
    @Min(0)
    private double height;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Purpose purpose;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
