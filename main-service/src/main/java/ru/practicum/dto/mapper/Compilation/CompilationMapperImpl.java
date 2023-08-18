package ru.practicum.dto.mapper.Compilation;

import org.springframework.stereotype.Component;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;

import java.util.List;
import java.util.Set;

@Component
public class CompilationMapperImpl implements CompilationMapper {
    @Override
    public Compilation compDtoToCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        return new Compilation(newCompilationDto.getTitle(), newCompilationDto.getPinned(), events);
    }

    @Override
    public CompilationDto compToDto(Compilation compilation, List<EventShortDto> events) {
        return new CompilationDto(compilation.getId(), compilation.getPinned(), compilation.getTitle(), events);
    }
}
