package ru.practicum.dto.mapper.Compilation;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;

import java.util.List;
import java.util.Set;

public interface CompilationMapper {
    Compilation compDtoToCompilation(NewCompilationDto newCompilationDto, Set<Event> events);

    CompilationDto compToDto(Compilation compilation, List<EventShortDto> events);
}
