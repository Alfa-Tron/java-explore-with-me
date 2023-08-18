package ru.practicum.admin.service.users;

import ru.practicum.dto.User.UserDto;
import ru.practicum.model.User;

import java.util.List;

public interface AdminServiceUsers {
    UserDto createUser(User user);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long userId);
}
