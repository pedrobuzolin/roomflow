package br.com.pedrobuzolin.roomflow.api.repository;

import br.com.pedrobuzolin.roomflow.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByNomeOrEmail(String nome, String email);
}
