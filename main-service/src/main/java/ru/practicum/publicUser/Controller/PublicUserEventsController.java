package ru.practicum.publicUser.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.model.EventSortType;
import ru.practicum.publicUser.service.events.PublicUserEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PublicUserEventsController {
    private final PublicUserEventService publicUserService;

    @GetMapping
    @Validated
    public List<EventShortDto> getEvents(@RequestParam(value = "text", required = false) String text,
                                         @RequestParam(value = "categories", required = false) List<Long> categories,
                                         @RequestParam(value = "paid", required = false) Boolean paid,
                                         @RequestParam(value = "rangeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStartString,
                                         @RequestParam(value = "rangeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEndString,
                                         @RequestParam(value = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(value = "sort", required = false) EventSortType sort,
                                         @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(value = "size", defaultValue = "10") @Positive Integer size,
                                         HttpServletRequest request) {

        return publicUserService.getEvents(text, categories, paid, rangeStartString, rangeEndString, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable long id, HttpServletRequest request) {
        return publicUserService.getEvent(id, request);

    }
}
