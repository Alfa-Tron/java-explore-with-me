package ru.practicum.dto.mapper.location;

import ru.practicum.dto.location.LocationDto;
import ru.practicum.model.Location;

public interface LocationMapper {
    LocationDto LcToLocationDto(Location location);

    Location DtoToLocation(LocationDto dto);
}
