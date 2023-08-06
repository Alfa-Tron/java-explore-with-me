package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StServerServiceImpl implements StServerService {
    @Autowired
    private StRepository stRepository;

    @Override
    @Transactional
    public void saveStats(EndpointHit endpointHitDtoToEndpointHit) {
        stRepository.save(endpointHitDtoToEndpointHit);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, boolean unique, List<String> uris) {

            if (unique && !uris.isEmpty()) {
                return stRepository.getUrisWithUniqueIP(start, end, uris);
            } else if (!uris.isEmpty()) {
                return stRepository.getUrisStats(start, end, uris);
            } else if (unique) {
                return stRepository.getAllUrisWithUniqueIP(start, end);
            } else {
                return stRepository.getAllUrisStats(start, end);
            }
    }

}
