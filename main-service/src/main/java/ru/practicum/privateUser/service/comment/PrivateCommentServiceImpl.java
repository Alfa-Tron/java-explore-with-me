package ru.practicum.privateUser.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.repository.CommentRepository;
import ru.practicum.admin.repository.EventRepository;
import ru.practicum.admin.repository.UserRepository;
import ru.practicum.dto.comment.CommentCreateDto;
import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.dto.mapper.comment.CommentMapper;
import ru.practicum.exeptions.AccessException;
import ru.practicum.exeptions.ObjectNotFoundException;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateCommentServiceImpl implements PrivateCommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentFullDto createComment(Long userId, Long eventId, CommentCreateDto requestCreateDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ObjectNotFoundException((String.format("Event with id=%s was not found", eventId))));
        Comment comment = new Comment();
        comment.setText(requestCreateDto.getText());
        comment.setActor(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);
        return commentMapper.commentToFullDto(comment);
    }

    @Override
    @Transactional
    public CommentFullDto patchComment(Long userId, Long eventId, Long commentId, CommentCreateDto commentCreateDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ObjectNotFoundException((String.format("Event with id=%s was not found", eventId))));

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Comment with id=%s was not found", commentId)));
        if (!Objects.equals(comment.getActor().getId(), userId) || !Objects.equals(comment.getEvent().getId(), eventId)) {
            throw new AccessException("not access");
        }
        comment.setText(commentCreateDto.getText());
        comment.setModified(LocalDateTime.now());
        return commentMapper.commentToFullDto(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long eventId, Long commentId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ObjectNotFoundException((String.format("Event with id=%s was not found", eventId))));
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Comment with id=%s was not found", commentId)));
        if (!Objects.equals(comment.getActor().getId(), userId) || !Objects.equals(comment.getEvent().getId(), eventId)) {
            throw new AccessException("not access");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentFullDto getComment(Long userId, Long eventId, Long commentId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ObjectNotFoundException((String.format("Event with id=%s was not found", eventId))));

        return commentMapper.commentToFullDto(commentRepository.findById(commentId).orElseThrow(() -> new ObjectNotFoundException(String.format("Comment with id=%s was not found", commentId))));
    }

    @Override
    public List<CommentFullDto> getCommentsByEvent(Long userId, Long eventId, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ObjectNotFoundException((String.format("Event with id=%s was not found", eventId))));

        List<Comment> comments = new ArrayList<>();
        for (Comment c : event.getComments()) {
            if (c.getActor() == user) {
                comments.add(c);
            }
        }
        if (from > comments.size()) return Collections.EMPTY_LIST;
        if (size > comments.size()) size = comments.size();
        List<Comment> subList = comments.subList(from, size);

        return commentMapper.commentListToDto(subList);
    }
}
