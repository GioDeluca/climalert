package ar.edu.utn.frba.ddsi.controller;


import ar.edu.utn.frba.ddsi.model.RegistroClima;
import ar.edu.utn.frba.ddsi.repository.ClimaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clima")
public class ClimaController {

  private final ClimaRepository repositorio;

  public ClimaController(ClimaRepository repositorio) {
    this.repositorio = repositorio;
  }

  @GetMapping("/ultimo")
  public ResponseEntity<RegistroClima> obtenerUltimo() {
    return repositorio.buscarUltimo().map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
  }

  @GetMapping("/historial")
  public ResponseEntity<List<RegistroClima>> obtenerHistorial() {
    List<RegistroClima> todos = repositorio.buscarTodos();
    if (todos.isEmpty()){
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(todos);
  }
}
