package com.gustavohenrique.gestao_processos.repository;

import com.gustavohenrique.gestao_processos.entity.UsuarioQuadro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioQuadroRepository extends JpaRepository<UsuarioQuadro, UUID>{
    Optional<UsuarioQuadro> findByUsuarioIdAndQuadroId(UUID usuarioID, UUID quadroId);

    // Verifica se um usuário está vinculado a um quadro
    boolean existsByUsuarioIdAndQuadroId(UUID usuarioId, UUID quadroId);

    // Verifica se um usuário é ADMIN em determinado quadro
    boolean existsByUsuarioIdAndQuadroIdAndRole(UUID usuarioId, UUID quadroId, UsuarioQuadro.Role role);

    // Lista todos os participantes de um quadro
    List<UsuarioQuadro> findByQuadroId(UUID quadroId);

    // Remove vínculo direto
    void deleteByUsuarioIdAndQuadroId(UUID usuarioId, UUID quadroId);
}
