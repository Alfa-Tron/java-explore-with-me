package ru.practicum.admin.service.events;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventsService {
    List<EventFullDto> findEvents(List<Long> userIds, List<EventState> states, List<Long> categoryIds, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);
}
