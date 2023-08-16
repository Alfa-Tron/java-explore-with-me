package ru.practicum.statClient;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointDtoIn;
import ru.practicum.model.Event;
import ru.practicum.statistic.StaticsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatClientServiceImpl implements StatClientService {
    private final StaticsClient statsClient;
    @Value("${name.ewm}")
    private String app;

    public Map<Long, Integer> getStatsFromEvents(List<Event> events) {
        Map<Long, Integer> hits = new HashMap<>();

        List<Long> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        List<String> uris = eventIds.stream()
                .map(i -> "/events/" + i)
                .collect(Collectors.toList());
        LocalDateTime st = null;
        for (Event e : events) {
            if (st == null || e.getPublishedOn().isBefore(st)) {
                st = e.getPublishedOn();
            }
        }
        if (st == null) return Collections.EMPTY_MAP;

        LocalDateTime start = LocalDateTime.now().minusYears(10);
        LocalDateTime end = LocalDateTime.now();

        ResponseEntity<Object> response = statsClient.getStats(start, end, true, uris);
        Object responseBody = response.getBody();
        List<Map<String, Object>> viewStatDtos = (List<Map<String, Object>>) responseBody;
        for (int i = 0; i < viewStatDtos.size(); i++) {
            String uri = (String) viewStatDtos.get(i).get("uri");
            Integer hit = (Integer) viewStatDtos.get(i).get("hits");
            hits.put(Long.parseLong(uri.substring(8)), hit);
        }
        return hits;
    }

    @Transactional

    public void createHit(HttpServletRequest request) {
        EndpointDtoIn endpointDtoIn = new EndpointDtoIn();
        endpointDtoIn.setApp(app);
        endpointDtoIn.setIp(request.getRemoteAddr());
        endpointDtoIn.setUri(request.getRequestURI());
        endpointDtoIn.setTimestamp(LocalDateTime.now());
        statsClient.saveStats(endpointDtoIn);
    }

}
