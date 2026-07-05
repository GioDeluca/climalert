package ar.edu.utn.frba.ddsi.scheduler;

import ar.edu.utn.frba.ddsi.service.ClimaAlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClimaScheduler {

    private final ClimaAlertService service;

  public ClimaScheduler(ClimaAlertService service) {
    this.service = service;
  }


  // Cada 5 minutos
    @Scheduled(fixedRate = 300000, initialDelay = 0)
    public void fetchClima() {
        service.obtenerYAlmacenar();
    }

    // Cada 1 minuto
    @Scheduled(fixedRate = 60000, initialDelay = 0)
    public void procesarAlertas() {
        service.procesarUltimoRegistro();
    }
}
