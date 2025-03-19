package ru.foodlog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.foodlog.dto.users.UserCreateDTO;
import ru.foodlog.dto.users.UserDTO;
import ru.foodlog.model.User;

/**
 * Интерфейс маппера для преобразования объектов User и UserDTO.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    /**
     * Преобразует UserCreateDTO в сущность User.
     *
     * @param userCreateDTO объект UserCreateDTO для преобразования
     * @return преобразованный объект User
     */
    User toUser(UserCreateDTO userCreateDTO);

    /**
     * Преобразует сущность User в UserDTO.
     *
     * @param user объект User для преобразования
     * @return преобразованный объект UserDTO
     */
    UserDTO toUserDto(User user);
}
