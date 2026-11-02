package br.com.pedrobuzolin.roomflow.api.repository;

import br.com.pedrobuzolin.roomflow.api.model.Sala;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    List<Sala> findAllByAtivaTrue();

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Sala s WHERE s.id = :id")
    Optional<Sala> buscarPorIdComBloqueio(@Param("id") Long id);
}
