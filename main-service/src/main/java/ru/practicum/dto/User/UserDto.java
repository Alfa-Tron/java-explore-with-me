package ru.practicum.dto.User;

import lombok.Getter;

@Getter
public class UserDto {
    private final String email;
    private final Long id;
    private final String name;

    public UserDto(long id, String email, String name) {
        this.email = email;
        this.id = id;
        this.name = name;
    }
}
