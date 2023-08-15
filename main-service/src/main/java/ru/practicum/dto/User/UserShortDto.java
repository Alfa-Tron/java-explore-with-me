package ru.practicum.dto.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserShortDto {
    private Long id;
    private String name;

    public UserShortDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
