package ru.practicum.publicUser.service.events;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.repository.CommentRepository;
import ru.practicum.admin.repository.EventRepository;
import ru.practicum.dto.comment.EventCommentProjection;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.mapper.caregory.CategoryMapper;
import ru.practicum.dto.mapper.event.EventMapper;
import ru.practicum.dto.mapper.location.LocationMapper;
import ru.practicum.dto.mapper.user.UserMapper;
import ru.practicum.exeptions.BadRequestException;
import ru.practicum.exeptions.ObjectNotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.EventSortType;
import ru.practicum.model.QEvent;
import ru.practicum.statClient.StatClientService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicUserEventServiceImpl implements PublicUserEventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final LocationMapper locationMapper;
    private final StatClientService statClientService;
    private final CommentRepository commentRepository;


    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, EventSortType sort, Integer from, Integer size, HttpServletRequest request) {

        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new BadRequestException(String.format("Start date=%s cannot be before end date=%s", rangeStart, rangeEnd));
            }
        }

        List<Event> result;
        BooleanBuilder builder = new BooleanBuilder();

        if (!Objects.isNull(text)) {
            builder.and((QEvent.event.annotation.containsIgnoreCase(text))).or(QEvent.event.description.containsIgnoreCase(text));
        }

        if (!Objects.isNull(paid)) {
            if (paid) {
                builder.and((QEvent.event.paid.isTrue()));
            } else {
                builder.and((QEvent.event.paid.isFalse()));
            }
        }

        if (!Objects.isNull(categories)) {
            builder.and((QEvent.event.category.id.in(categories)));
        }

        if (Objects.isNull(rangeStart) && Objects.isNull(rangeEnd)) {
            builder.and((QEvent.event.eventDate.after(LocalDateTime.now())));
        } else {
            builder.and(QEvent.event.eventDate.after(rangeStart)).and(QEvent.event.eventDate.before(rangeEnd));
        }

        if (onlyAvailable) {
            builder.and(QEvent.event.participantLimit.goe(0));
        }
        Sort eventSort = Sort.by(Sort.Direction.ASC, "eventDate");
        if (Objects.equals(sort, EventSortType.VIEWS)) {
            eventSort = Sort.by(Sort.Direction.ASC, "views");
        }

        Pageable pageable = PageRequest.of(from, size, eventSort);

        Iterable<Event> events = eventRepository.findAll(builder, pageable);
        result = StreamSupport.stream(events.spliterator(), false).collect(Collectors.toList());

        statClientService.createHit(request);

        Map<Long, Integer> hits = Collections.EMPTY_MAP;
        if (!result.isEmpty()) {
            hits = statClientService.getStatsFromEvents(result);
        }
        List<Long> ids = new ArrayList<>();
        for (Event e : events) {
            ids.add(e.getId());
        }

        List<EventCommentProjection> commentProjections = commentRepository.getCommentCountsForEvents(ids);
        Map<Long, Long> eventCommentCounts = new HashMap<>();
        for (EventCommentProjection projection : commentProjections) {
            eventCommentCounts.put(projection.getEventId(), projection.getCount());
        }

        List<EventShortDto> eventShortDtos = result.stream().map(e -> eventMapper.eventToShortDto(e, categoryMapper.categoryToDTO(e.getCategory()), userMapper.userToUserShort(e.getInitiator()), eventCommentCounts.get(e.getId()))).collect(Collectors.toList());

        for (EventShortDto eventShortDto : eventShortDtos) {
            eventShortDto.setViews(hits.getOrDefault(eventShortDto.getId(), 0));
        }

        return eventShortDtos;
    }


    @Override
    public EventFullDto getEvent(long id, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndPublished(id).orElseThrow(() -> new ObjectNotFoundException(String.format("Event with id=%s was not found", id)));
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        List<EventCommentProjection> commentProjections = commentRepository.getCommentCountsForEvents(ids);
        Map<Long, Long> eventCommentCounts = new HashMap<>();
        for (EventCommentProjection projection : commentProjections) {
            eventCommentCounts.put(projection.getEventId(), projection.getCount());
        }

        EventFullDto eventFullDto = eventMapper.toFullEventDto(event, categoryMapper.categoryToDTO(event.getCategory()), locationMapper.lcToLocationDto(event.getLocation()), userMapper.userToUserShort(event.getInitiator()), eventCommentCounts.get(id));

        statClientService.createHit(request);

        Map<Long, Integer> hits = statClientService.getStatsFromEvents(List.of(event));

        eventFullDto.setViews(hits.get(id));

        return eventFullDto;
    }
}
