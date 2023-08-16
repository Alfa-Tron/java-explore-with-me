package ru.practicum.dto.view;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewStatDto {
    private String app;
    private String uri;
    private Long hits;
}