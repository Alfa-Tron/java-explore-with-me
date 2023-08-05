package ru.practicum.statistic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointDtoIn;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Validated
public class StaticsController {
    private final StaticsClient staticsClient;

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam(name = "start") String start,
                                           @RequestParam(name = "end") String end,
                                           @RequestParam(name = "unique", defaultValue = "false") boolean unique,
                                           @RequestParam(name = "uris", required = false) List<String> uris) {

        return staticsClient.getStats(start, end, unique, uris);
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> saveStats(@RequestBody @Valid EndpointDtoIn endpointDtoIn) {
        return staticsClient.saveStats(endpointDtoIn);

    }
}
