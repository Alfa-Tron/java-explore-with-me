package ru.practicum.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentDto {
    private long id;
    private String text;
    private long userId;

    public CommentDto(long id, String text, long userId) {
        this.id = id;
        this.text = text;
        this.userId = userId;
    }
}
