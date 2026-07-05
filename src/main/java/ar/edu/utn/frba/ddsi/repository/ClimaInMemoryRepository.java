package ar.edu.utn.frba.ddsi.repository;


import ar.edu.utn.frba.ddsi.model.RegistroClima;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ClimaInMemoryRepository implements ClimaRepository {

    private final List<RegistroClima> registros = new ArrayList<>();

    @Override
    public void guardar(RegistroClima registro) {
        registros.add(registro);
    }

    @Override
    public Optional<RegistroClima> buscarUltimo() {
        if (registros.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(registros.get(registros.size() - 1));
    }

    @Override
    public List<RegistroClima> buscarTodos() {
        return registros;
    }
}
