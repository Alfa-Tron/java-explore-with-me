package ru.practicum.privateUser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.repository.*;
import ru.practicum.dto.Requests.EventRequestStatusUpdateRequest;
import ru.practicum.dto.Requests.EventRequestStatusUpdateResult;
import ru.practicum.dto.Requests.ParticipationRequestDto;
import ru.practicum.dto.User.UserShortDto;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.mapper.caregory.CategoryMapper;
import ru.practicum.dto.mapper.event.EventMapper;
import ru.practicum.dto.mapper.location.LocationMapper;
import ru.practicum.dto.mapper.participation.ParticipationRequestMapper;
import ru.practicum.dto.mapper.user.UserMapper;
import ru.practicum.dto.view.ViewStatDto;
import ru.practicum.exeptions.BadRequestException;
import ru.practicum.exeptions.ConflictException;
import ru.practicum.exeptions.ObjectNotFoundException;
import ru.practicum.model.*;
import ru.practicum.statistic.StaticsClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateUserServiceImpl implements PrivateUserService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;
    private final CategoryMapper categoryMapper;
    private final LocationMapper locationMapper;
    private final UserMapper userMapper;
    private final StaticsClient statsClient;
    private final RequestRepository requestRepository;
    private final ParticipationRequestMapper participationRequestMapper;

    @Override
    @Transactional
    public EventFullDto createNewEvent(Long userId, NewEventDto newEventDto) {

        validateEventDate(newEventDto.getEventDate());

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));

        Location location = getLocationOrAddNew(newEventDto.getLocation());
        LocationDto locationDto = locationMapper.lcToLocationDto(location);
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new ObjectNotFoundException(String.format("Category with id=%s was not found", newEventDto.getCategory())));
        CategoryDto categoryDto = categoryMapper.categoryToDTO(category);
        Event event = eventMapper.newEventDtoToEvent(newEventDto, category, location, user);
        UserShortDto userShortDto = userMapper.userToUserShort(user);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toFullEventDto(savedEvent, categoryDto, locationDto, userShortDto);

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

    private void validateEventDate(LocalDateTime eventDate) {
        if (LocalDateTime.now().plusHours(2).isAfter(eventDate)) {
            throw new BadRequestException(String.format("Event date=%s cannot be before now + 2 hours date.", eventDate));
        }
    }


    @Override
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));

        Pageable pageable = PageRequest.of(from, size);

        List<Event> events = eventRepository.findByInitiatorIdOrderByEventDateDesc(userId, pageable);

        Map<Long, Integer> hits = getStatsFromEvents(events);

        return events.stream()
                .map(e -> eventMapper.eventToShortDto(e, categoryMapper.categoryToDTO(e.getCategory()), userMapper.userToUserShort(e.getInitiator())))
                .peek(e -> e.setViews(hits.getOrDefault(e.getId(), 0)))
                .collect(Collectors.toList());
    }

    private Map<Long, Integer> getStatsFromEvents(List<Event> events) {
        Map<Long, Integer> hits = new HashMap<>();

        List<Long> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        List<String> uris = eventIds.stream()
                .map(i -> "/events/" + i)
                .collect(Collectors.toList());
        LocalDateTime start = LocalDateTime.now().minusYears(10);
        LocalDateTime end = LocalDateTime.now();

        ResponseEntity<Object> response = statsClient.getStats(start, end, true, uris);
        Object responseBody = response.getBody();
        List<ViewStatDto> viewStatDtos = (List<ViewStatDto>) responseBody;
        for (ViewStatDto viewStatDto : viewStatDtos) {
            String uri = viewStatDto.getUri();
            hits.put(Long.parseLong(uri.substring(8)), Math.toIntExact(viewStatDto.getHits()));
        }
        return hits;
    }

    @Override
    public EventFullDto getOneEvent(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Event with id=%s was not found", eventId)));

        Map<Long, Integer> hits = getStatsFromEvents(List.of(event));

        EventFullDto eventFullDto = eventMapper.toFullEventDto(event,
                categoryMapper.categoryToDTO(event.getCategory()),
                locationMapper.lcToLocationDto(event.getLocation()),
                userMapper.userToUserShort(event.getInitiator()));

        eventFullDto.setViews(hits.getOrDefault(eventId, 0));

        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto patchEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Event with id=%s was not found", eventId)));

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException(
                    String.format("User with id=%s is not initiator of event with id=%s", userId, eventId));
        }

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }

        updateEventParams(updateEventUserRequest, event);
        if (!Objects.isNull(updateEventUserRequest.getStateAction())) {
            StateAction state = updateEventUserRequest.getStateAction();
            switch (state) {
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                default:
                    throw new ConflictException("Only pending or canceled events can be changed");
            }
        }

        Event updatedEvent = eventRepository.save(event);

        Map<Long, Integer> hits = getStatsFromEvents(List.of(event));

        EventFullDto eventFullDto = eventMapper.toFullEventDto(updatedEvent,
                categoryMapper.categoryToDTO(updatedEvent.getCategory()),
                locationMapper.lcToLocationDto(updatedEvent.getLocation()),
                userMapper.userToUserShort(updatedEvent.getInitiator()));

        eventFullDto.setViews(hits.getOrDefault(eventId, 0));

        return eventFullDto;
    }


    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        List<Request> requests = requestRepository.findByEventInitiatorIdAndEventId(userId, eventId);

        return requests.stream()
                .map(participationRequestMapper::requestToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult patchRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest requestStatusUpdateRequest) {

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Event with id=%s was not found", eventId)));

        if (event.getParticipants().size() >= event.getParticipantLimit()) {
            throw new ConflictException("The participant limit has been reached");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("You can't participate in an unpublished event");
        }

        List<Request> requests = requestRepository.findAllById(requestStatusUpdateRequest.getRequestIds());

        requests.forEach(r -> {
            if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
                return;
            }
            if (RequestStatus.REJECTED == requestStatusUpdateRequest.getStatus()) {
                r.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(participationRequestMapper.requestToDto(r));
            }
            if (RequestStatus.CONFIRMED == requestStatusUpdateRequest.getStatus()) {
                r.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(participationRequestMapper.requestToDto(r));
            }
        });

        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Event with id=%s was not found", eventId)));

        Request requestExist = requestRepository.findOneByEventIdAndRequesterId(eventId, userId);

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Cant make request for unpublished request");
        }

        if (!Objects.isNull(requestExist)) {
            throw new ConflictException(String.format("Event with id=%s and requester with id=%s already exist",
                    eventId, userId));
        }

        if (event.getInitiator().getId() == userId.intValue()) {
            throw new ConflictException(String.format("Event initiator with id=%s cant make request for their event",
                    userId));
        }

        if (event.getParticipantLimit() != 0 &&
                event.getParticipants().size() >= event.getParticipantLimit()) {
            throw new ConflictException("Participant limit for request is exceeded");
        }

        Request request = new Request(event, user, LocalDateTime.now());


        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }

        return participationRequestMapper.requestToDto(requestRepository.save(request));
    }


    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));

        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Request with id=%s was not found", requestId)));

        if (user.getId() != (request.getRequester().getId())) {
            throw new ConflictException(String.format("User with id=%s can't cancel request with id=%s",
                    userId, requestId));
        }

        request.setStatus(RequestStatus.CANCELED);

        return participationRequestMapper.requestToDto(requestRepository.save(request));
    }


    @Override
    public List<ParticipationRequestDto> getRequestById(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));

        List<Request> requests = requestRepository.findByRequesterId(userId);

        return requests.stream()
                .map(participationRequestMapper::requestToDto)
                .collect(Collectors.toList());
    }

    private void updateEventParams(UpdateEventUserRequest updateEventRequest, Event event) {

        if (!Objects.isNull(updateEventRequest.getAnnotation())) {
            if (updateEventRequest.getAnnotation().length() < 20 ||
                    updateEventRequest.getAnnotation().length() > 2000) {
                throw new BadRequestException("Annotation length cannot be less than 20 or more than 2000");
            }
            event.setAnnotation(updateEventRequest.getAnnotation());
        }

        if (!Objects.isNull(updateEventRequest.getCategory())) {
            Category category = categoryRepository.findById(updateEventRequest.getCategory()).orElseThrow(() -> new ObjectNotFoundException(String.format("Category with id=%s was not found",
                    updateEventRequest.getCategory())));
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


}
