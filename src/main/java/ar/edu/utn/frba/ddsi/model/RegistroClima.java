package ar.edu.utn.frba.ddsi.model;

import java.time.LocalDateTime;

public record RegistroClima(
    String ubicacion,
    double temperatura,
    double humedad,
    String condicion,
    double vientoKph,
    LocalDateTime observadoEn
) {
}
