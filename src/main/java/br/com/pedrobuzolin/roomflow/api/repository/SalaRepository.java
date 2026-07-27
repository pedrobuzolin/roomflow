package br.com.pedrobuzolin.roomflow.api.repository;

import br.com.pedrobuzolin.roomflow.api.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    List<Sala> findAllByAtivaTrue();

    boolean existsByNome(String nome);
}
