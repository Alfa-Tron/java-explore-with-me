package ru.practicum.admin.service.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.admin.repository.CommentRepository;
import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.dto.mapper.comment.CommentMapper;
import ru.practicum.exeptions.ObjectNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCommentsServiceImpl implements AdminCommentsService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public void deleteComment(Long comId) {
        if (commentRepository.existsById(comId))
            commentRepository.deleteById(comId);
        else throw new ObjectNotFoundException("comment not found");
    }

    @Override
    public List<CommentFullDto> findAll(List<Long> users, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        return commentMapper.commentListToDto(commentRepository.findAllByActorIdIn(users, pageable));
    }
}
