package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StRepository extends JpaRepository<EndpointHit,Long> {
    @Query("SELECT new ru.practicum.model.ViewStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip)) FROM EndpointHit AS h WHERE h.timestamp BETWEEN :start AND :end AND h.uri in :uris GROUP BY h.app, h.uri ORDER BY COUNT(h.ip) DESC")
    List<ViewStatsDto> getUrisWithUniqueIP(
             LocalDateTime start,
             LocalDateTime end,
             List<String> uris
    );

    @Query("SELECT new ru.practicum.model.ViewStatsDto(h.app, h.uri, COUNT(h.id)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.id) DESC")
    List<ViewStatsDto> getUrisStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );


    @Query("SELECT new ru.practicum.model.ViewStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStatsDto> getAllUrisWithUniqueIP(
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("SELECT new ru.practicum.model.ViewStatsDto(h.app, h.uri, COUNT(h.id)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +    // Включаем поле h.app в GROUP BY
            "ORDER BY COUNT(h.id) DESC")
    List<ViewStatsDto> getAllUrisStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );


}
