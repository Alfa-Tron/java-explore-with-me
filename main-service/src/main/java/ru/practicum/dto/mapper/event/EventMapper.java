package ru.practicum.dto.mapper.event;

import ru.practicum.dto.User.UserShortDto;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;

import java.util.List;

public interface EventMapper {
    Event newEventDtoToEvent(NewEventDto newEventDto, Category category, Location location, User user);

    EventFullDto toFullEventDto(Event savedEvent, CategoryDto category, LocationDto locationDto, UserShortDto userShortDto, List<CommentDto> commentDtoList);

    EventShortDto eventToShortDto(Event e, CategoryDto categoryDto, UserShortDto userDto);
}
