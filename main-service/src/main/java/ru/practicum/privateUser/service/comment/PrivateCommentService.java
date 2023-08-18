package ru.practicum.privateUser.service.comment;

import ru.practicum.dto.comment.CommentCreateDto;
import ru.practicum.dto.comment.CommentFullDto;

import java.util.List;

public interface PrivateCommentService {
    CommentFullDto createComment(Long userId, Long eventId, CommentCreateDto requestCreateDto);

    CommentFullDto patchComment(Long userId, Long eventId, Long commentId, CommentCreateDto commentCreateDto);

    void deleteComment(Long userId, Long eventId, Long commentId);

    CommentFullDto getComment(Long userId, Long eventId, Long commentId);

    List<CommentFullDto> getCommentsByEvent(Long userId, Long eventId, int from, int size);
}
