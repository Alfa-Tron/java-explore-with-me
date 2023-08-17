package ru.practicum.privateUser.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.Requests.ParticipationRequestDto;
import ru.practicum.privateUser.service.requests.PrivateUserRequestsService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateRequestsController {
    private final PrivateUserRequestsService privateUserService;

    @GetMapping
    public List<ParticipationRequestDto> getRequestById(@PathVariable Long userId) {
        return privateUserService.getRequestById(userId);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId, @RequestParam(value = "eventId") Long eventId) {

        return privateUserService.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        return privateUserService.cancelRequest(userId, requestId);
    }
}
