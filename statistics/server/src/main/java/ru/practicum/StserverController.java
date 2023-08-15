package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Mapper.EndPointMapper;
import ru.practicum.model.ViewStatsDto;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StserverController {
    private final StServerService stServerService;
    private final EndPointMapper endpointMapper;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam(name = "start") String start,
                                       @RequestParam(name = "end") String end,
                                       @RequestParam(name = "unique", defaultValue = "false") boolean unique,
                                       @RequestParam(name = "uris", required = false) List<String> uris) {
        String s = URLDecoder.decode(start, StandardCharsets.UTF_8);
        String e = URLDecoder.decode(end, StandardCharsets.UTF_8);
        String ss = URLDecoder.decode(s, StandardCharsets.UTF_8);// не понимаю почему приходится 2 раза декодировать
        String ee = URLDecoder.decode(e, StandardCharsets.UTF_8);
        LocalDateTime start1 = LocalDateTime.parse(ss, formatter);
        LocalDateTime end1 = LocalDateTime.parse(ee, formatter);


        return stServerService.getStats(start1, end1, unique, uris);
    }

    @PostMapping("/hit")
    public void saveStats(@RequestBody EndpointDtoIn endpointDtoIn) {
        stServerService.saveStats(endpointMapper.endpointHitDtoToEndpointHit(endpointDtoIn));

    }
}