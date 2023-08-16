package ru.practicum.publicUser.service.events;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.model.EventSortType;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PublicUserEventService {

    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStartString, LocalDateTime rangeEndString, Boolean onlyAvailable, EventSortType sort, Integer from, Integer size, HttpServletRequest request);

    EventFullDto getEvent(long id, HttpServletRequest request);
}
