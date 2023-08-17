package ru.practicum.admin.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.service.events.AdminEventsService;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.model.EventState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final AdminEventsService adminEventsService;

    @GetMapping
    @Validated
    public List<EventFullDto> findEvents(@RequestParam(name = "users", required = false) List<Long> userIds,
                                         @RequestParam(name = "states", required = false) List<EventState> states,
                                         @RequestParam(name = "categories", required = false) List<Long> categoryIds,
                                         @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(name = "size", defaultValue = "10") @Positive int size) {

        return adminEventsService.findEvents(userIds, states, categoryIds, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId, @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        return adminEventsService.updateEvent(eventId, updateEventAdminRequest);
    }

}
