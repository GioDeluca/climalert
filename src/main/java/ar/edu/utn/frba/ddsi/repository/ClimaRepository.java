package ar.edu.utn.frba.ddsi.repository;

import ar.edu.utn.frba.ddsi.model.RegistroClima;
import java.util.List;
import java.util.Optional;

public interface ClimaRepository {
    void guardar(RegistroClima registro);
    Optional<RegistroClima> buscarUltimo();
    List<RegistroClima> buscarTodos();
}
