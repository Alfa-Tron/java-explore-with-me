package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;


@Getter
@Setter
public class EndpointDtoIn {
    private String app;
    private String uri;
    private String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
