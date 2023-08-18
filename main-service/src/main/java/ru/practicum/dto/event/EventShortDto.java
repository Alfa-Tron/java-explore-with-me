package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.dto.User.UserShortDto;
import ru.practicum.dto.category.CategoryDto;

import java.time.LocalDateTime;

@Setter
@Getter
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private Integer views;

    public EventShortDto(String annotation, CategoryDto category, Integer confirmedRequests, LocalDateTime eventDate, Long id, UserShortDto initiator, boolean paid, String title) {
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.eventDate = eventDate;
        this.id = id;
        this.initiator = initiator;
        this.paid = paid;
        this.title = title;
    }
}
