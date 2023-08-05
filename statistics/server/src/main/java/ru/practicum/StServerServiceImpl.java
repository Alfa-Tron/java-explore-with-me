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
    public List<ViewStatsDto> getStats(String start, String end, boolean unique, List<String> uris) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startBd = LocalDateTime.parse(start, formatter);
        LocalDateTime endBd = LocalDateTime.parse(end, formatter);
        {
            if (unique && !uris.isEmpty()) {
                return stRepository.getUrisWithUniqueIP(startBd, endBd, uris);
            } else if (!uris.isEmpty()) {
                return stRepository.getUrisStats(startBd, endBd, uris);
            } else if (unique) {
                return stRepository.getAllUrisWithUniqueIP(startBd, endBd);
            } else {
                return stRepository.getAllUrisStats(startBd, endBd);
            }
        }
    }

}
