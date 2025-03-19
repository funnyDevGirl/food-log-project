package ru.foodlog.dto.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.foodlog.enums.Gender;
import ru.foodlog.enums.Purpose;

@Getter
@Setter
public class UserDTO {

    private long id;

    private String email;

    private String name;

    private int age;

    private double weight;

    private double height;

    private Purpose purpose;

    private Gender gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String createdAt;
}
