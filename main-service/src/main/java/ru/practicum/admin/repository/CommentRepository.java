package ru.practicum.admin.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.comment.EventCommentProjection;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEvent_Id(Long id, Pageable pageable);

    @Query("SELECT c.event.id as eventId, COUNT(c.id) as commentCount FROM comments c WHERE c.event.id IN :eventIds GROUP BY c.event.id")
    List<EventCommentProjection> getCommentCountsForEvents(@Param("eventIds") List<Long> eventIds);

    List<Comment> findByEventAndActor(Event event, User user);

    List<Comment> findAllByActorIdIn(List<Long> actorIds, Pageable pageable);

}
