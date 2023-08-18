package ru.practicum.dto.event;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.dto.User.UserShortDto;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class EventFullDto {
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocationDto location;
    private boolean paid;
    private int participantLimit;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private EventState state;
    private String title;
    private Integer views;
    private List<CommentDto> comments;

    public EventFullDto(String annotation, CategoryDto category, int confirmedRequests, LocalDateTime createdOn, String description, LocalDateTime eventDate, Long id, UserShortDto initiator, LocationDto location, boolean paid, int participantLimit, LocalDateTime publishedOn, boolean requestModeration, EventState state, String title, Integer views, List<CommentDto> comments) {
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.createdOn = createdOn;
        this.description = description;
        this.eventDate = eventDate;
        this.id = id;
        this.initiator = initiator;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        this.requestModeration = requestModeration;
        this.state = state;
        this.title = title;
        this.views = views;
        this.comments = comments;
    }

}
