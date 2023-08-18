package ru.practicum.statClient;

import ru.practicum.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface StatClientService {
    Map<Long, Integer> getStatsFromEvents(List<Event> events);

    void createHit(HttpServletRequest request);


}
