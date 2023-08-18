package ru.practicum.dto.comment;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentFullDto {
    private Long id;
    private String text;
    private User actor;
    private Long eventId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modified;

    public CommentFullDto(Long id, String text, User actor, Long eventId, LocalDateTime created, LocalDateTime modified) {
        this.id = id;
        this.text = text;
        this.actor = actor;
        this.eventId = eventId;
        this.created = created;
        this.modified = modified;
    }
}