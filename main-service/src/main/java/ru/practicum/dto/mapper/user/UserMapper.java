package ru.practicum.dto.mapper.user;

import ru.practicum.dto.User.NewUserRequest;
import ru.practicum.dto.User.UserDto;
import ru.practicum.dto.User.UserShortDto;
import ru.practicum.model.User;

import java.util.List;

public interface UserMapper {
    User newUserRequestToUser(NewUserRequest newUserRequest);

    UserDto userToDto(User user);

    List<UserDto> usersToDtos(List<User> users);

    UserShortDto userToUserShort(User user);
}
