package ru.practicum.publicUser.service.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.repository.CommentRepository;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.mapper.comment.CommentMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicUserCommentsServiceImpl implements PublicUserCommentsService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentDto> getComments(Long eventId, int from, int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by("created").ascending());
        return commentMapper.commentListToDtos(commentRepository.findAllByEvent_Id(eventId, pageable));
    }
}
