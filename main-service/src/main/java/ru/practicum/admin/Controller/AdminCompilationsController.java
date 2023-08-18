package ru.practicum.admin.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.service.compilations.AdminServiceCompilation;
import ru.practicum.dto.Requests.UpdateCompilationRequest;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Validated
public class AdminCompilationsController {
    private final AdminServiceCompilation adminServiceCompilation;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return adminServiceCompilation.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComp(@PathVariable Long compId) {
        adminServiceCompilation.deleteComp(compId);

    }

    @PatchMapping("/{compId}")
    public CompilationDto updateComp(@PathVariable Long compId, @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        return adminServiceCompilation.updateComp(compId, updateCompilationRequest);
    }


}
