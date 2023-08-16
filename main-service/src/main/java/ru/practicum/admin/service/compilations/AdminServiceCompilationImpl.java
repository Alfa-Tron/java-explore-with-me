package ru.practicum.admin.service.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.repository.CompilationRepository;
import ru.practicum.admin.repository.EventRepository;
import ru.practicum.dto.Requests.UpdateCompilationRequest;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.mapper.Compilation.CompilationMapper;
import ru.practicum.dto.mapper.caregory.CategoryMapper;
import ru.practicum.dto.mapper.event.EventMapper;
import ru.practicum.dto.mapper.user.UserMapper;
import ru.practicum.exeptions.ObjectNotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceCompilationImpl implements AdminServiceCompilation {
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;
    private final CompilationRepository compilationRepository;
    private final EventMapper eventMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;


    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>();

        if (!Objects.isNull(newCompilationDto.getEvents())) {
            events = eventRepository.getByIdIn(newCompilationDto.getEvents());
        }

        Compilation compilation = compilationMapper.compDtoToCompilation(newCompilationDto, events);

        Compilation savedCompilation = compilationRepository.save(compilation);

        return compilationMapper.compToDto(savedCompilation, events.stream()
                .map(e -> eventMapper.eventToShortDto(e,
                        categoryMapper.categoryToDTO(e.getCategory()),
                        userMapper.userToUserShort(e.getInitiator())))
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void deleteComp(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() -> new ObjectNotFoundException(String.format("Compilation with id=%s was not found", compId)));
        compilationRepository.deleteById(compId);
    }


    @Override
    @Transactional
    public CompilationDto updateComp(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new ObjectNotFoundException(String.format("Compilation with id=%s was not found", compId)));

        Set<Event> events = new HashSet<>();

        if (!Objects.isNull(updateCompilationRequest.getEvents())) {
            events = eventRepository.getByIdIn(updateCompilationRequest.getEvents());
        }
        compilation.setEvents(events);

        if (!Objects.isNull(updateCompilationRequest.getTitle()) && !updateCompilationRequest.getTitle().isBlank()) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }

        if (!Objects.isNull(updateCompilationRequest.getPinned())) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }

        Compilation savedCompilation = compilationRepository.save(compilation);

        return compilationMapper.compToDto(savedCompilation, events.stream()
                .map(e -> eventMapper.eventToShortDto(e,
                        categoryMapper.categoryToDTO(e.getCategory()),
                        userMapper.userToUserShort(e.getInitiator())))
                .collect(Collectors.toList()));
    }
}

