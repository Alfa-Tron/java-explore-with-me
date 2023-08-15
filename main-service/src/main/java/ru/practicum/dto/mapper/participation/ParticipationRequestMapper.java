package ru.practicum.dto.mapper.participation;

import ru.practicum.dto.Requests.ParticipationRequestDto;
import ru.practicum.model.Request;

public interface ParticipationRequestMapper {
    ParticipationRequestDto requestToDto(Request request);
}
