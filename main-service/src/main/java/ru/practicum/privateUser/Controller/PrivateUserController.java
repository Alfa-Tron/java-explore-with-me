package ru.practicum.privateUser.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.Requests.EventRequestStatusUpdateRequest;
import ru.practicum.dto.Requests.EventRequestStatusUpdateResult;
import ru.practicum.dto.Requests.ParticipationRequestDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.privateUser.service.PrivateUserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateUserController {
    private final PrivateUserService privateUserService;

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createNewEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {

        return privateUserService.createNewEvent(userId, newEventDto);

    }

    @GetMapping("/events")
    public List<EventShortDto> getEvents(@PathVariable Long userId, @RequestParam(name = "from", defaultValue = "0") Integer from, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return privateUserService.getUserEvents(userId, from, size);

    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getOneEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return privateUserService.getOneEvent(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto patchEvent(@PathVariable Long userId, @PathVariable Long eventId, @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return privateUserService.patchEvent(userId, eventId, updateEventUserRequest);

    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return privateUserService.getRequests(userId, eventId);


    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult patchRequest(@PathVariable Long userId, @PathVariable Long eventId, @Valid @RequestBody EventRequestStatusUpdateRequest requestStatusUpdateRequest) {
        return privateUserService.patchRequest(userId, eventId, requestStatusUpdateRequest);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getRequestById(@PathVariable Long userId) {
        return privateUserService.getRequestById(userId);

    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId, @RequestParam(value = "eventId") Long eventId) {

        return privateUserService.createRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        return privateUserService.cancelRequest(userId, requestId);
    }


}
