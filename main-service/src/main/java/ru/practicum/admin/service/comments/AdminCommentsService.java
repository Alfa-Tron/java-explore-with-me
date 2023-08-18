package ru.practicum.admin.service.comments;

import ru.practicum.dto.comment.CommentFullDto;

import java.util.List;

public interface AdminCommentsService {
    void deleteComment(Long comId);

    List<CommentFullDto> findAll(List<Long> users, Integer from, Integer size);
}
