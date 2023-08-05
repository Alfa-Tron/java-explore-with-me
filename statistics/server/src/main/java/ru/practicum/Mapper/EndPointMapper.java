package ru.practicum.Mapper;

import ru.practicum.EndpointDtoIn;
import ru.practicum.model.EndpointHit;

public interface EndPointMapper {
    EndpointHit endpointHitDtoToEndpointHit(EndpointDtoIn endpointDtoIn);

}
