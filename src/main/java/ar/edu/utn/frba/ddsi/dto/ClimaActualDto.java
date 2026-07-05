package ar.edu.utn.frba.ddsi.dto;

public record ClimaActualDto(
        String last_updated,
        double temp_c,
        double humidity,
        double wind_kph,
        CondicionDto condition
) {}
