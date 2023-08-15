package ru.practicum.publicUser.service;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.model.EventSortType;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PublicUserService {
    List<CompilationDto> getCompilations(boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compId);

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getOneCategory(Long catId);

    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStartString, LocalDateTime rangeEndString, Boolean onlyAvailable, EventSortType sort, Integer from, Integer size, HttpServletRequest request);

    EventFullDto getEvent(long id, HttpServletRequest request);
}
