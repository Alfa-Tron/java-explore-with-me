package ru.practicum.privateUser.Controller;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.privateUser.service.event.PrivateUserEventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class PrivateEventController {
    private final PrivateUserEventService privateUserService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createNewEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {

        return privateUserService.createNewEvent(userId, newEventDto);

    }

    @GetMapping
    public List<EventShortDto> getEvents(@PathVariable Long userId, @RequestParam(name = "from", defaultValue = "0") Integer from, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return privateUserService.getUserEvents(userId, from, size);

    }

    @GetMapping("/{eventId}")
    public EventFullDto getOneEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return privateUserService.getOneEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(@PathVariable Long userId, @PathVariable Long eventId, @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return privateUserService.patchEvent(userId, eventId, updateEventUserRequest);

    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return privateUserService.getRequests(userId, eventId);


    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult patchRequest(@PathVariable Long userId, @PathVariable Long eventId, @Valid @RequestBody EventRequestStatusUpdateRequest requestStatusUpdateRequest) {
        return privateUserService.patchRequest(userId, eventId, requestStatusUpdateRequest);
    }

}
