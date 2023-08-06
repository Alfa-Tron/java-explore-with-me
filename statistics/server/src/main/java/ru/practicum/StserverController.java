package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Mapper.EndPointMapper;
import ru.practicum.model.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StserverController {
    private final StServerService stServerService;
    private final EndPointMapper endpointMapper;

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                       @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime end,
                                       @RequestParam(name = "unique", defaultValue = "false") boolean unique,
                                       @RequestParam(name = "uris", required = false) List<String> uris) {

        return stServerService.getStats(start, end, unique, uris);
    }

    @PostMapping("/hit")
    public void saveStats(@RequestBody EndpointDtoIn endpointDtoIn) {
        stServerService.saveStats(endpointMapper.endpointHitDtoToEndpointHit(endpointDtoIn));

    }
}