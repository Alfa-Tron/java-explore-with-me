package ru.practicum.dto.mapper.event;

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

public interface EventMapper {
    Event NewEventDtoToEvent(NewEventDto newEventDto, Category category, Location location, User user);

    EventFullDto ToFullEventDto(Event savedEvent, CategoryDto category, LocationDto locationDto, UserShortDto userShortDto);

    EventShortDto EventToShortDto(Event e, CategoryDto categoryDto, UserShortDto userDto);
}
