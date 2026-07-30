package br.com.pedrobuzolin.roomflow.api.repository;

import br.com.pedrobuzolin.roomflow.api.model.Reserva;
import br.com.pedrobuzolin.roomflow.api.model.Sala;
import br.com.pedrobuzolin.roomflow.api.model.StatusReserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("""
        SELECT r FROM Reserva r
        WHERE r.statusReserva = :status
        AND r.dataInicio < :fim
        AND r.dataFim > :inicio
        """)
    List<Reserva> buscarReservasConflitantes(
            @Param("status") StatusReserva status,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    @Query("""
        SELECT COUNT(r) > 0 FROM Reserva r
        WHERE r.statusReserva = :status
        AND r.sala.id = :idSala
        AND r.dataInicio < :fim
        AND r.dataFim > :inicio
        """)
    boolean buscarReservaConflitantePorSalaEHorario(
            @Param("status") StatusReserva status,
            @Param("idSala")  Long idSala,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    Page<Reserva> findByStatusReserva(StatusReserva statusReserva, Pageable pageable);

    Page<Reserva> findAll(Pageable pageable);

    Page<Reserva> findBySala(Sala sala, Pageable pageable);

    @Query(value = """
        SELECT r FROM Reserva r
        WHERE r.dataInicio < :fim
        AND r.dataFim > :inicio
        """,
    countQuery = """
        SELECT COUNT(r) FROM Reserva r
        WHERE r.dataInicio < :fim
        AND r.dataFim > :inicio
        """)
    Page<Reserva> buscarPorPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            Pageable pageable);
}
