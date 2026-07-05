package ar.edu.utn.frba.ddsi.client;

import ar.edu.utn.frba.ddsi.dto.WeatherApiResponse;
import ar.edu.utn.frba.ddsi.model.RegistroClima;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.stereotype.Component;

@Component
public class MapperClimatico {

    public RegistroClima mapear(WeatherApiResponse respuesta) {
        return new RegistroClima(
            respuesta.location().name(),
            respuesta.current().temp_c(),
            respuesta.current().humidity(),
            respuesta.current().condition().text(),
            respuesta.current().wind_kph(),
            this.parsearFecha(respuesta.current().last_updated())
        );
    }

    private LocalDateTime parsearFecha(String fecha) {
        if (fecha == null || fecha.isBlank()) {
            return LocalDateTime.now();
        }
        try {
            return LocalDateTime.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm"));
        } catch (DateTimeParseException e) {
            return LocalDateTime.now();
        }
    }
}
