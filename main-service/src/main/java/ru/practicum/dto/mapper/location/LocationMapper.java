package ru.practicum.dto.mapper.location;

import ru.practicum.dto.location.LocationDto;
import ru.practicum.model.Location;

public interface LocationMapper {
    LocationDto lcToLocationDto(Location location);

    Location dtoToLocation(LocationDto dto);
}
