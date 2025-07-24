package com.gustavohenrique.gestao_processos.repository;

import com.gustavohenrique.gestao_processos.entity.Processo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProcessoRepository extends JpaRepository<Processo, UUID> {
    List<Processo> findByQuadroId(UUID quadroId);
}
