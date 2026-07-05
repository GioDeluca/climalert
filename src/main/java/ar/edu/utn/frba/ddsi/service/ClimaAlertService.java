package ar.edu.utn.frba.ddsi.service;

import ar.edu.utn.frba.ddsi.client.WeatherApiClient;
import ar.edu.utn.frba.ddsi.model.RegistroClima;
import ar.edu.utn.frba.ddsi.repository.ClimaRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ClimaAlertService {

  private static final double TEMPERATURA_MAXIMA = 35.0;
  private static final double HUMEDAD_MAXIMA = 60.0;
  private final WeatherApiClient cliente;
  private final ClimaRepository repositorio;

  public ClimaAlertService(WeatherApiClient cliente, ClimaRepository repositorio) {
    this.cliente = cliente;
    this.repositorio = repositorio;
  }

  public void obtenerYAlmacenar() {
    try {
      RegistroClima registro = cliente.obtenerClimaActual();
      repositorio.guardar(registro);
    } catch (Exception ex) {
      throw new RuntimeException("Fallo al obtener datos climaticos", ex);
    }
  }

  public void procesarUltimoRegistro() {
    Optional<RegistroClima> ultimo = repositorio.buscarUltimo();

    if (ultimo.isEmpty()) {
      return;
    }

    RegistroClima registro = ultimo.get();

    if (esCondicionCritica(registro)) {
      this.enviarAlerta(registro);
    } 
    else {
      
    }
  }

  private void enviarAlerta(RegistroClima registro) {
  }

  private boolean esCondicionCritica(RegistroClima registro) {
    return registro.temperatura() > TEMPERATURA_MAXIMA && registro.humedad() > HUMEDAD_MAXIMA;
  }
}
