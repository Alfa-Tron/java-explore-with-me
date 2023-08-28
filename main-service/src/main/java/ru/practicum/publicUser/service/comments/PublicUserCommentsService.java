package ru.practicum.publicUser.service.comments;

import ru.practicum.dto.comment.CommentDto;

import java.util.List;

public interface PublicUserCommentsService {
    List<CommentDto> getComments(Long eventId, int from, int size);
}
