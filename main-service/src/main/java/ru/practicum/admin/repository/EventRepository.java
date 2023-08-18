package ru.practicum.admin.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.Event;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    List<Event> findByInitiatorIdOrderByEventDateDesc(Long userId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    @Query(value = "SELECT e FROM Event e " +
            "WHERE e.id = :eventId " +
            "AND e.state = 'PUBLISHED'")
    Optional<Event> findByIdAndPublished(Long eventId);

    Set<Event> getByIdIn(Collection<Long> ids);
}