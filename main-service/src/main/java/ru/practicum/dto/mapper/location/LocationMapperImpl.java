package ru.practicum.dto.mapper.location;

import org.springframework.stereotype.Component;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.model.Location;

@Component
public class LocationMapperImpl implements LocationMapper {
    @Override
    public LocationDto LcToLocationDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }

    @Override
    public Location DtoToLocation(LocationDto dto) {
        return new Location(dto.getLat(), dto.getLon());
    }
}
