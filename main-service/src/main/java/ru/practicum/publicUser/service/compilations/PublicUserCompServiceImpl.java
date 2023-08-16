package ru.practicum.publicUser.service.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.repository.CompilationRepository;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.mapper.Compilation.CompilationMapper;
import ru.practicum.dto.mapper.caregory.CategoryMapper;
import ru.practicum.dto.mapper.event.EventMapper;
import ru.practicum.dto.mapper.user.UserMapper;
import ru.practicum.exeptions.ObjectNotFoundException;
import ru.practicum.model.Compilation;

import java.util.List;
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


    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Compilation> compilations;
        compilations = compilationRepository.findAllByPinned(pinned, pageable);

        return compilations.stream()
                .map(c -> compilationMapper.compToDto(c, c.getEvents().stream()
                        .map(e -> eventMapper.eventToShortDto(e, categoryMapper.categoryToDTO(e.getCategory()), userMapper.userToUserShort(e.getInitiator())))
                        .collect(Collectors.toList()))).collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {

        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new ObjectNotFoundException(String.format("Compilation with id=%s was not found", compId)));
        return compilationMapper.compToDto(compilation, compilation.getEvents().stream()
                .map(e -> eventMapper.eventToShortDto(e,
                        categoryMapper.categoryToDTO(e.getCategory()),
                        userMapper.userToUserShort(e.getInitiator())))
                .collect(Collectors.toList()));
    }

}
