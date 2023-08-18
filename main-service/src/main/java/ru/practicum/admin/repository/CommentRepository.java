package ru.practicum.admin.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEvent_Id(Long id, Pageable pageable);

    List<Comment> findAllByEventIn(List<Event> events);

    List<Comment> findAllByActorIdIn(List<Long> actorIds, Pageable pageable);

}
