package ru.practicum.privateUser.service.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.repository.EventRepository;
import ru.practicum.admin.repository.RequestRepository;
import ru.practicum.admin.repository.UserRepository;
import ru.practicum.dto.Requests.ParticipationRequestDto;
import ru.practicum.dto.mapper.participation.ParticipationRequestMapper;
import ru.practicum.exeptions.ConflictException;
import ru.practicum.exeptions.ObjectNotFoundException;
import ru.practicum.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateUserRequestsServiceImpl implements PrivateUserRequestsService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final ParticipationRequestMapper participationRequestMapper;

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Event with id=%s was not found", eventId)));

        Request requestExist = requestRepository.findOneByEventIdAndRequesterId(eventId, userId);

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Cant make request for unpublished request");
        }

        if (!Objects.isNull(requestExist)) {
            throw new ConflictException(String.format("Event with id=%s and requester with id=%s already exist",
                    eventId, userId));
        }

        if (event.getInitiator().getId() == userId.intValue()) {
            throw new ConflictException(String.format("Event initiator with id=%s cant make request for their event",
                    userId));
        }

        if (event.getParticipantLimit() != 0 &&
                event.getParticipants().size() >= event.getParticipantLimit()) {
            throw new ConflictException("Participant limit for request is exceeded");
        }

        Request request = new Request(event, user, LocalDateTime.now());


        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }

        return participationRequestMapper.requestToDto(requestRepository.save(request));
    }


    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));

        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Request with id=%s was not found", requestId)));

        if (!user.getId().equals(request.getRequester().getId())) {
            throw new ConflictException(String.format("User with id=%s can't cancel request with id=%s",
                    userId, requestId));
        }

        request.setStatus(RequestStatus.CANCELED);

        return participationRequestMapper.requestToDto(requestRepository.save(request));
    }


    @Override
    public List<ParticipationRequestDto> getRequestById(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));

        List<Request> requests = requestRepository.findByRequesterId(userId);

        return requests.stream()
                .map(participationRequestMapper::requestToDto)
                .collect(Collectors.toList());
    }

}
