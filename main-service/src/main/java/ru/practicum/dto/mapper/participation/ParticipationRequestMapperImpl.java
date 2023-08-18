package ru.practicum.dto.mapper.participation;

import org.springframework.stereotype.Component;
import ru.practicum.dto.Requests.ParticipationRequestDto;
import ru.practicum.model.Request;

@Component
public class ParticipationRequestMapperImpl implements ParticipationRequestMapper {
    @Override
    public ParticipationRequestDto requestToDto(Request request) {
        return new ParticipationRequestDto(request.getId(), request.getCreated(), request.getEvent().getId(), request.getRequester().getId(), request.getStatus());
    }
}

