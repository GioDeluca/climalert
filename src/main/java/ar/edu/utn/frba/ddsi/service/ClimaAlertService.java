package ar.edu.utn.frba.ddsi.service;

import ar.edu.utn.frba.ddsi.client.WeatherApiClient;
import ar.edu.utn.frba.ddsi.model.RegistroClima;
import ar.edu.utn.frba.ddsi.repository.ClimaRepository;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ClimaAlertService {

  private static final double TEMPERATURA_MAXIMA = 35.0;
  private static final double HUMEDAD_MAXIMA = 60.0;
  private final WeatherApiClient cliente;
  private final ClimaRepository repositorio;

  private static final List<String> DESTINATARIOS = List.of(
      "admin@clima.com",
      "emergencias@clima.com",
      "meteorologia@clima.com"
  );

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String mailFrom;

  public ClimaAlertService(WeatherApiClient cliente, ClimaRepository repositorio, JavaMailSender mailSender) {
    this.cliente = cliente;
    this.repositorio = repositorio;
    this.mailSender = mailSender;
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

  public void enviarAlerta(RegistroClima registro) {
    SimpleMailMessage mensaje = new SimpleMailMessage();
    mensaje.setFrom(mailFrom);
    mensaje.setTo(DESTINATARIOS.toArray(new String[0]));
    mensaje.setSubject(construirAsunto(registro));
    mensaje.setText(construirCuerpo(registro));

    try {
      mailSender.send(mensaje);

    } catch (MailException ex) {
      throw new RuntimeException("No se pudo enviar el correo", ex);
    }
  }

  private String construirAsunto(RegistroClima r) {
    return "[Climalert] Alerta climatica en %s - %.1f C / %.0f%% humedad"
        .formatted(r.ubicacion(), r.temperatura(), r.humedad());
  }

  private String construirCuerpo(RegistroClima r) {
    return """
                ALERTA CLIMATICA - Climalert

                Hay condiciones climaticas criticas en la ciudad: %s.
                Detalle completo del clima:
                ------------------------------------------
              
                Temperatura:          %.1f C
                Humedad:              %.0f%%
                Condicion:            %s
                Velocidad del viento: %.1f km/h
                Fecha/hora informada: %s
                
                ------------------------------------------
                Si mas tarde persisten las condiciones criticas, le informaremos!
                """.formatted(r.ubicacion(), r.temperatura(), r.humedad(),
        r.condicion(), r.vientoKph(), r.observadoEn().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
    );
  }

  private boolean esCondicionCritica(RegistroClima registro) {
    return registro.temperatura() > TEMPERATURA_MAXIMA && registro.humedad() > HUMEDAD_MAXIMA;
  }
}
