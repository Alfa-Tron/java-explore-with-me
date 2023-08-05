package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Mapper.EndPointMapper;
import ru.practicum.model.ViewStatsDto;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class StserverController {
    private final StServerService stServerService;
    private final EndPointMapper endpointMapper;

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam(name = "start") String start,
                                       @RequestParam(name = "end") String end,
                                       @RequestParam(name = "unique", defaultValue = "false") boolean unique,
                                       @RequestParam(name = "uris", required = false) List<String> uris) {
        if (uris == null) {
            uris = Collections.emptyList();
        }
        return stServerService.getStats(start, end, unique, uris);
    }

    @PostMapping("/hit")
    public void saveStats(@RequestBody EndpointDtoIn endpointDtoIn) {
        stServerService.saveStats(endpointMapper.endpointHitDtoToEndpointHit(endpointDtoIn));

    }
}