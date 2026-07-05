package ar.edu.utn.frba.ddsi.dto;


public record WeatherApiResponse(
        UbicacionDto location,
        ClimaActualDto current
) {}
