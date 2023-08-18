package ru.practicum.admin.service.compilations;

import ru.practicum.dto.Requests.UpdateCompilationRequest;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;

public interface AdminServiceCompilation {

    void deleteComp(Long compId);

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    CompilationDto updateComp(Long compId, UpdateCompilationRequest updateCompilationRequest);
}
