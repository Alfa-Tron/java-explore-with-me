package ru.practicum.privateUser.service;

import ru.practicum.dto.Requests.EventRequestStatusUpdateRequest;
import ru.practicum.dto.Requests.EventRequestStatusUpdateResult;
import ru.practicum.dto.Requests.ParticipationRequestDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;

import java.util.List;

public interface PrivateUserService {
    EventFullDto createNewEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size);

    EventFullDto getOneEvent(Long userId, Long eventId);

    EventFullDto patchEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult patchRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest requestStatusUpdateRequest);

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getRequestById(Long userId);
}
