package ru.practicum.privateUser.service.requests;

import ru.practicum.dto.Requests.ParticipationRequestDto;

import java.util.List;

public interface PrivateUserRequestsService {
    ParticipationRequestDto createRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getRequestById(Long userId);
}
