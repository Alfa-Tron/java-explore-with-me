package ru.practicum.dto.compilation;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.dto.event.EventShortDto;

import java.util.List;

@Getter
@Setter
public class CompilationDto {
    private Long id;
    private String title;
    private Boolean pinned;
    private List<EventShortDto> events;

    public CompilationDto(Long id, Boolean pinned, String title, List<EventShortDto> events) {
        this.id = id;
        this.pinned = pinned;
        this.title = title;
        this.events = events;
    }
}
