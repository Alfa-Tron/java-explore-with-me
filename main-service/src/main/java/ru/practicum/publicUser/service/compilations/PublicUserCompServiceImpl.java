package ru.practicum.publicUser.service.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.repository.CommentRepository;
import ru.practicum.admin.repository.CompilationRepository;
import ru.practicum.dto.comment.EventCommentProjection;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.mapper.Compilation.CompilationMapper;
import ru.practicum.dto.mapper.caregory.CategoryMapper;
import ru.practicum.dto.mapper.event.EventMapper;
import ru.practicum.dto.mapper.user.UserMapper;
import ru.practicum.exeptions.ObjectNotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicUserCompServiceImpl implements PublicUserCompService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final CommentRepository commentRepository;


    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(pageable).getContent();
        } else {
            compilations = compilationRepository.findAllByPinned(pinned, pageable);
        }

        List<Long> ids = new ArrayList<>();
        for (Compilation c : compilations) {
            for (Event e : c.getEvents())// чет тут перебор будто бы полчился с циклами
                if (!ids.contains(e.getId()))
                    ids.add(e.getId());
        }
        List<EventCommentProjection> commentProjections = commentRepository.getCommentCountsForEvents(ids);
        Map<Long, Long> eventCommentCounts = new HashMap<>();
        for (EventCommentProjection projection : commentProjections) {
            eventCommentCounts.put(projection.getEventId(), projection.getCount());
        }


        return compilations.stream()
                .map(c -> compilationMapper.compToDto(c, c.getEvents().stream()
                        .map(e -> eventMapper.eventToShortDto(e, categoryMapper.categoryToDTO(e.getCategory()), userMapper.userToUserShort(e.getInitiator()), eventCommentCounts.get(e.getId())))
                        .collect(Collectors.toList()))).collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {

        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new ObjectNotFoundException(String.format("Compilation with id=%s was not found", compId)));
        List<Long> ids = new ArrayList<>();
        for (Event e : compilation.getEvents()) {
            ids.add(e.getId());
        }
        List<EventCommentProjection> commentProjections = commentRepository.getCommentCountsForEvents(ids);
        Map<Long, Long> eventCommentCounts = new HashMap<>();
        for (EventCommentProjection projection : commentProjections) {
            eventCommentCounts.put(projection.getEventId(), projection.getCount());
        }
        return compilationMapper.compToDto(compilation, compilation.getEvents().stream()
                .map(e -> eventMapper.eventToShortDto(e,
                        categoryMapper.categoryToDTO(e.getCategory()),
                        userMapper.userToUserShort(e.getInitiator()),
                        eventCommentCounts.get(e.getId())))
                .collect(Collectors.toList()));
    }

}
