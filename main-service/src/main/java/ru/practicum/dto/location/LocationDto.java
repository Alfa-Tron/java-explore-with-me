package ru.practicum.dto.location;

import lombok.Getter;

@Getter
public class LocationDto {
    private final float lat;
    private final float lon;

    public LocationDto(Float lat, Float lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
