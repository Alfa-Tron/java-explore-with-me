package ru.practicum;

import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStatsDto;

import java.util.List;

public interface StServerService {
    void saveStats(EndpointHit endpointHitDtoToEndpointHit);

    List<ViewStatsDto> getStats(String start, String end, boolean unique, List<String> uris);
}
