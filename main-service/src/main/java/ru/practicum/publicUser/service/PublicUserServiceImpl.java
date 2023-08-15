package ru.practicum.publicUser.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointDtoIn;
import ru.practicum.admin.repository.CategoryRepository;
import ru.practicum.admin.repository.CompilationRepository;
import ru.practicum.admin.repository.EventRepository;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.mapper.Compilation.CompilationMapper;
import ru.practicum.dto.mapper.caregory.CategoryMapper;
import ru.practicum.dto.mapper.event.EventMapper;
import ru.practicum.dto.mapper.location.LocationMapper;
import ru.practicum.dto.mapper.user.UserMapper;
import ru.practicum.exeptions.BadRequestException;
import ru.practicum.exeptions.ObjectNotFoundException;
import ru.practicum.model.*;
import ru.practicum.statistic.StaticsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicUserServiceImpl implements PublicUserService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final LocationMapper locationMapper;
    private final CategoryRepository categoryRepository;
    private final StaticsClient staticsClient;

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);

        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);

        return compilations.stream()
                .map(c -> compilationMapper.CompToDto(c, c.getEvents().stream()
                        .map(e -> eventMapper.EventToShortDto(e, categoryMapper.categoryToDTO(e.getCategory()), userMapper.userToUserShort(e.getInitiator())))
                        .collect(Collectors.toList()))).collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {

        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new ObjectNotFoundException(String.format("Compilation with id=%s was not found", compId)));
        return compilationMapper.CompToDto(compilation, compilation.getEvents().stream()
                .map(e -> eventMapper.EventToShortDto(e,
                        categoryMapper.categoryToDTO(e.getCategory()),
                        userMapper.userToUserShort(e.getInitiator())))
                .collect(Collectors.toList()));
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);

        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::categoryToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getOneCategory(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new ObjectNotFoundException(String.format("Category with id=%s was not found", catId)));

        return categoryMapper.categoryToDTO(category);
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, EventSortType sort, Integer from, Integer size, HttpServletRequest request) {

        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new BadRequestException(String.format("Start date=%s cannot be before end date=%s",
                        rangeStart, rangeEnd));
            }
        }

        List<Event> result;
        BooleanBuilder builder = new BooleanBuilder();

        if (!Objects.isNull(text)) {
            builder.and((QEvent.event.annotation.containsIgnoreCase(text)))
                    .or(QEvent.event.description.containsIgnoreCase(text));
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
            builder.and(QEvent.event.eventDate.after(rangeStart))
                    .and(QEvent.event.eventDate.before(rangeEnd));
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

        createHit(request);

        Map<Long, Integer> hits = new HashMap<>();
        if (!result.isEmpty()) {
            hits = getStatsFromEvents(result);
        }
        List<EventShortDto> eventShortDtos = result.stream()
                .map(e -> eventMapper.EventToShortDto(e, categoryMapper.categoryToDTO(e.getCategory()), userMapper.userToUserShort(e.getInitiator())))
                .collect(Collectors.toList());

        for (EventShortDto eventShortDto : eventShortDtos) {
            eventShortDto.setViews(hits.getOrDefault(eventShortDto.getId(), 0));
        }

        return eventShortDtos;
    }

    private Map<Long, Integer> getStatsFromEvents(List<Event> events) {
        Map<Long, Integer> hits = new HashMap<>();

        List<Long> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        List<String> uris = eventIds.stream()
                .map(i -> "/events/" + i)
                .collect(Collectors.toList());

        LocalDateTime start = LocalDateTime.now().minusYears(30);
        LocalDateTime end = LocalDateTime.now();

        ResponseEntity<Object> response = staticsClient.getStats(start, end, true, uris);
        Object responseBody = response.getBody();
        List<Map<String, Object>> viewStatDtos = (List<Map<String, Object>>) responseBody;
        for (int i = 0; i < viewStatDtos.size(); i++) {
            String uri = (String) viewStatDtos.get(i).get("uri");
            Integer hit = (Integer) viewStatDtos.get(i).get("hits");
            hits.put(Long.parseLong(uri.substring(8)), hit);
        }
        return hits;
    }

    private void createHit(HttpServletRequest request) {
        String app = "ewm-main-service";
        EndpointDtoIn endpointDtoIn = new EndpointDtoIn();
        endpointDtoIn.setApp(app);
        endpointDtoIn.setIp(request.getRemoteAddr());
        endpointDtoIn.setUri(request.getRequestURI());
        endpointDtoIn.setTimestamp(LocalDateTime.now());
        staticsClient.saveStats(endpointDtoIn);
    }

    @Override
    public EventFullDto getEvent(long id, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndPublished(id).orElseThrow(() ->
                new ObjectNotFoundException(String.format("Event with id=%s was not found", id)));

        EventFullDto eventFullDto = eventMapper.ToFullEventDto(event,
                categoryMapper.categoryToDTO(event.getCategory()),
                locationMapper.LcToLocationDto(event.getLocation()),
                userMapper.userToUserShort(event.getInitiator()));

        createHit(request);

        Map<Long, Integer> hits = getStatsFromEvents(List.of(event));

        eventFullDto.setViews(hits.get(id));

        return eventFullDto;
    }
}