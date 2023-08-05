package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;


@Getter
@Setter
public class EndpointDtoIn {
    @NotNull
    private Integer id;
    @NotNull
    private String app;
    @NotNull
    private  String uri;
    @NotNull
    private String ip;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
