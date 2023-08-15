package ru.practicum.dto.view;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewStatDto {
    String app;
    String uri;
    Long hits;
}