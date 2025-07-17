package com.gustavohenrique.gestao_processos.repository;

import com.gustavohenrique.gestao_processos.entity.Quadro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuadroRepository extends JpaRepository<Quadro, UUID> {
    List<Quadro> findByUsuario(UUID usuarioId);
}
