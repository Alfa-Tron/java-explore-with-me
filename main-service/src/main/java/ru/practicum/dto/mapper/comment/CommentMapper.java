package ru.practicum.dto.mapper.comment;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.model.Comment;

import java.util.List;

public interface CommentMapper {
    CommentFullDto commentToFullDto(Comment comment);

    List<CommentFullDto> commentListToDto(List<Comment> allByEventId);

    CommentDto commentToDto(Comment comment);

    List<CommentDto> commentListToDtos(List<Comment> comments);
}
