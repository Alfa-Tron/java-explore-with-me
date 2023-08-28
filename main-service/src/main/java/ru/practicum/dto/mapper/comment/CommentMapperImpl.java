package ru.practicum.dto.mapper.comment;

import org.springframework.stereotype.Component;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapperImpl implements CommentMapper {
    @Override
    public CommentFullDto commentToFullDto(Comment comment) {
        return new CommentFullDto(comment.getId(), comment.getText(), comment.getActor().getId(), comment.getEvent().getId(), comment.getCreated(), comment.getModified());
    }

    @Override
    public List<CommentFullDto> commentListToDto(List<Comment> allByEventId) {
        return allByEventId.stream()
                .map(this::commentToFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto commentToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getActor().getId());
    }

    @Override
    public List<CommentDto> commentListToDtos(List<Comment> comments) {
        return comments.stream()
                .map(this::commentToDto)
                .collect(Collectors.toList());
    }


}
