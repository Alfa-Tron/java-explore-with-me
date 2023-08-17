package ru.practicum.publicUser.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.publicUser.service.compilations.PublicUserCompService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class PublicUserCompilationController {
    private final PublicUserCompService publicUserService;

    @GetMapping
    @Validated
    public List<CompilationDto> getCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned, @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from, @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        return publicUserService.getCompilations(pinned, from, size);

    }

    @GetMapping("/{compId}")
    public CompilationDto getOneCompilation(@PathVariable Long compId) {
        return publicUserService.getCompilationById(compId);

    }
}
