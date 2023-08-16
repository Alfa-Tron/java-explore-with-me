package ru.practicum.publicUser.service.compilations;

import ru.practicum.dto.compilation.CompilationDto;

import java.util.List;

public interface PublicUserCompService {
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compId);
}
