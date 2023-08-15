package ru.practicum.dto.Requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;

    private List<ParticipationRequestDto> rejectedRequests;

    public EventRequestStatusUpdateResult(List<ParticipationRequestDto> confirmedRequests, List<ParticipationRequestDto> rejectedRequests) {
        this.confirmedRequests = confirmedRequests;
        this.rejectedRequests = rejectedRequests;
    }
}