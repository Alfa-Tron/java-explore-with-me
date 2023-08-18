package ru.practicum.dto.mapper.user;

import org.springframework.stereotype.Component;
import ru.practicum.dto.User.NewUserRequest;
import ru.practicum.dto.User.UserDto;
import ru.practicum.dto.User.UserShortDto;
import ru.practicum.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User newUserRequestToUser(NewUserRequest newUserRequest) {
        return new User(newUserRequest.getEmail(), newUserRequest.getName());
    }

    @Override
    public UserDto userToDto(User user) {
        return new UserDto(user.getId().intValue(), user.getEmail(), user.getName());
    }

    @Override
    public List<UserDto> usersToDtos(List<User> users) {
        return users.stream()
                .map(this::userToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserShortDto userToUserShort(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
