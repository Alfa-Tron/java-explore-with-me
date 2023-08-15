package ru.practicum.dto.mapper.event;

import org.springframework.stereotype.Component;
import ru.practicum.dto.User.UserShortDto;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;

@Component
public class EventMapperImpl implements EventMapper {


    @Override
    public Event newEventDtoToEvent(NewEventDto dto, Category category, Location location, User user) {
        return new Event(dto.getAnnotation(), dto.getDescription(), dto.getEventDate(), dto.getPaid(), dto.getParticipantLimit(), dto.getRequestModeration(), dto.getTitle(), category, location, user);
    }

    @Override
    public EventFullDto toFullEventDto(Event savedEvent, CategoryDto category, LocationDto locationDto, UserShortDto userShortDto) {
        return new EventFullDto(savedEvent.getAnnotation(), category, savedEvent.getParticipants().size(), savedEvent.getCreatedOn(), savedEvent.getDescription(), savedEvent.getEventDate(), savedEvent.getId(), userShortDto, locationDto, savedEvent.getPaid(), savedEvent.getParticipantLimit(), savedEvent.getPublishedOn(), savedEvent.getRequestModeration(), savedEvent.getState(), savedEvent.getTitle(), 0);
    }

    @Override
    public EventShortDto eventToShortDto(Event e, CategoryDto categoryDto, UserShortDto userDto) {
        return new EventShortDto(e.getAnnotation(), categoryDto, 0, e.getEventDate(), e.getId(), userDto, e.getPaid(), e.getTitle());
    }
}
