package ru.practicum.admin.service.events;

import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.repository.CategoryRepository;
import ru.practicum.admin.repository.EventRepository;
import ru.practicum.admin.repository.LocationRepository;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.mapper.caregory.CategoryMapper;
import ru.practicum.dto.mapper.event.EventMapper;
import ru.practicum.dto.mapper.location.LocationMapper;
import ru.practicum.dto.mapper.user.UserMapper;
import ru.practicum.exeptions.BadRequestException;
import ru.practicum.exeptions.ConflictException;
import ru.practicum.exeptions.ObjectNotFoundException;
import ru.practicum.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AdminEventsServiceIml implements AdminEventsService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final LocationMapper locationMapper;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    @Override
    public List<EventFullDto> findEvents(List<Long> userIds, List<EventState> states, List<Long> categoryIds, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        BooleanBuilder builder = new BooleanBuilder();
        List<Event> result;
        if (!Objects.isNull(userIds)) {
            builder.and(QEvent.event.initiator.id.in(userIds));
        }
        if (!Objects.isNull(states)) {
            builder.and(QEvent.event.state.in(states));
        }
        if (!Objects.isNull(categoryIds)) {
            builder.and(QEvent.event.category.id.in(categoryIds));
        }
        if (!Objects.isNull(rangeStart)) {
            builder.and(QEvent.event.eventDate.after(rangeStart));
        }
        if (!Objects.isNull(rangeEnd)) {
            builder.and(QEvent.event.eventDate.before(rangeEnd));
        }

        Pageable pageable = PageRequest.of(from, size);
        Iterable<Event> events = eventRepository.findAll(builder, pageable);
        result = StreamSupport.stream(events.spliterator(), false).collect(Collectors.toList());

        return result.stream()
                .map(e -> eventMapper.toFullEventDto(e,
                        categoryMapper.categoryToDTO(e.getCategory()),
                        locationMapper.lcToLocationDto(e.getLocation()),
                        userMapper.userToUserShort(e.getInitiator())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Event with id=%s was not found", eventId)));

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }

        updateEventParams(updateEventAdminRequest, event);

        if (!Objects.isNull(updateEventAdminRequest.getStateAction())) {
            StateAction state = updateEventAdminRequest.getStateAction();
            switch (state) {
                case PUBLISH_EVENT:
                    if (event.getState() != EventState.PENDING) {
                        throw new ConflictException("Cannot publish the event because it's not in the right state: PUBLISHED");
                    }
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
                default:
                    throw new ConflictException("Only pending or canceled events can be changed");
            }
        }

        Event updatedEvent = eventRepository.save(event);

        return eventMapper.toFullEventDto(updatedEvent,
                categoryMapper.categoryToDTO(updatedEvent.getCategory()),
                locationMapper.lcToLocationDto(updatedEvent.getLocation()),
                userMapper.userToUserShort(updatedEvent.getInitiator()));
    }

    private void updateEventParams(UpdateEventAdminRequest updateEventRequest, Event event) {
        if (!Objects.isNull(updateEventRequest.getAnnotation())) {
            if (updateEventRequest.getAnnotation().length() < 20 ||
                    updateEventRequest.getAnnotation().length() > 2000) {
                throw new BadRequestException("Annotation length cannot be less than 20 or more than 2000");
            }
            event.setAnnotation(updateEventRequest.getAnnotation());
        }

        if (!Objects.isNull(updateEventRequest.getCategory())) {
            Category category = categoryRepository.findById(updateEventRequest.getCategory()).orElseThrow(() -> {
                throw new ObjectNotFoundException(String.format("Category with id=%s was not found",
                        updateEventRequest.getCategory()));
            });
            event.setCategory(category);
        }

        if (!Objects.isNull(updateEventRequest.getDescription())) {
            if (updateEventRequest.getDescription().length() < 20 ||
                    updateEventRequest.getDescription().length() > 7000) {
                throw new BadRequestException("Description length cannot be less than 20 or more than 7000");
            }
            event.setDescription(updateEventRequest.getDescription());
        }

        if (!Objects.isNull(updateEventRequest.getEventDate())) {
            validateEventDate(updateEventRequest.getEventDate());
            event.setEventDate(updateEventRequest.getEventDate());
        }

        if (!Objects.isNull(updateEventRequest.getLocation())) {
            Location foundOrAddedLocation = getLocationOrAddNew(updateEventRequest.getLocation());
            event.setLocation(foundOrAddedLocation);
        }

        if (!Objects.isNull(updateEventRequest.getPaid())) {
            event.setPaid(updateEventRequest.getPaid());
        }

        if (!Objects.isNull(updateEventRequest.getParticipantLimit())) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }

        if (!Objects.isNull(updateEventRequest.getRequestModeration())) {
            event.setRequestModeration(updateEventRequest.getRequestModeration());
        }

        if (!Objects.isNull(updateEventRequest.getTitle())) {
            if (updateEventRequest.getTitle().length() < 3 ||
                    updateEventRequest.getTitle().length() > 120) {
                throw new BadRequestException("Title length cannot be less than 3 or more than 120");
            }
            event.setTitle(updateEventRequest.getTitle());
        }
    }

    private void validateEventDate(LocalDateTime eventDate) {
        if (LocalDateTime.now().plusHours(2).isAfter(eventDate)) {
            throw new BadRequestException(String.format("Event date=%s cannot be before now + 2 hours date.", eventDate));
        }
    }

    private Location getLocationOrAddNew(LocationDto locationDto) {
        Location location = locationRepository.findByLatAndLon(
                locationDto.getLat(),
                locationDto.getLon());

        if (Objects.isNull(location)) {
            location = locationRepository.save(locationMapper.dtoToLocation(locationDto));
        }
        return location;
    }

}
