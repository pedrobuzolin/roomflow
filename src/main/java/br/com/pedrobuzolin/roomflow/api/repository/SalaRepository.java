package br.com.pedrobuzolin.roomflow.api.repository;

import br.com.pedrobuzolin.roomflow.api.model.Sala;
import br.com.pedrobuzolin.roomflow.api.model.StatusReserva;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    List<Sala> findAllByAtivaTrue();

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Sala s WHERE s.id = :id")
    Optional<Sala> buscarPorIdComBloqueio(@Param("id") Long id);

    @Query(
            value = """
        SELECT s
        FROM Sala s
        WHERE s.ativa = true
        AND NOT EXISTS (
            SELECT r.id
            FROM Reserva r
            WHERE r.sala = s
            AND r.statusReserva = :status
            AND r.dataInicio < :dataFim
            AND r.dataFim > :dataInicio
        )
    """,
            countQuery = """
        SELECT COUNT(s)
        FROM Sala s
        WHERE s.ativa = true
        AND NOT EXISTS (
            SELECT r.id
            FROM Reserva r
            WHERE r.sala = s
            AND r.statusReserva = :status
            AND r.dataInicio < :dataFim
            AND r.dataFim > :dataInicio
        )
    """
    )
    Page<Sala> buscarSalasDisponiveis(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("status") StatusReserva status,
            Pageable pageable
    );
}
