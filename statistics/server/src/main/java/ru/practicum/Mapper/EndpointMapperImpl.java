package ru.practicum.Mapper;

import org.springframework.stereotype.Component;
import ru.practicum.EndpointDtoIn;
import ru.practicum.model.EndpointHit;

@Component
public class EndpointMapperImpl implements EndPointMapper {

    @Override
    public EndpointHit endpointHitDtoToEndpointHit(EndpointDtoIn endpointDtoIn) {
        return new EndpointHit(endpointDtoIn.getApp(), endpointDtoIn.getUri(), endpointDtoIn.getIp(), endpointDtoIn.getTimestamp());
    }
}
