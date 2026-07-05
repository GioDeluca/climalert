package ar.edu.utn.frba.ddsi.client;

import ar.edu.utn.frba.ddsi.dto.WeatherApiResponse;
import ar.edu.utn.frba.ddsi.model.RegistroClima;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@Slf4j
public class WeatherApiClient {

    private static final String BASE_URL = "https://api.weatherapi.com/v1";
    private static final String UBICACION = "Buenos Aires";

    private final RestClient restClient;
    private final MapperClimatico mapper;

    @Value("${climalert.weather-api.api-key}")
    private String apiKey;

    public WeatherApiClient(MapperClimatico mapper) {
        this.restClient = RestClient.builder().baseUrl(BASE_URL).build();
        this.mapper = mapper;
    }

    public RegistroClima obtenerClimaActual() {
        try {
            WeatherApiResponse respuesta = restClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/current.json")
                    .queryParam("key", apiKey)
                    .queryParam("q", UBICACION)
                    .queryParam("aqi", "no")
                    .build())
                .retrieve()
                .body(WeatherApiResponse.class);

            if (respuesta == null) {
                throw new RuntimeException("WeatherAPI devolvio una respuesta nula");
            }
            return mapper.mapear(respuesta);
        } catch (RestClientException ex) {
            throw new RuntimeException("Error al consultar WeatherAPI ", ex);
        }
    }
}
