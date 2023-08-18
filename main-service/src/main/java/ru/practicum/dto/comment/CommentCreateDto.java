package ru.practicum.dto.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentCreateDto {
    @NotBlank
    @Size(min = 1, max = 1024)
    private String text;
}
