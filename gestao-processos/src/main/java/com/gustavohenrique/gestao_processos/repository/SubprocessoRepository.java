package com.gustavohenrique.gestao_processos.repository;

import com.gustavohenrique.gestao_processos.entity.SubProcesso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubprocessoRepository extends JpaRepository<SubProcesso, UUID> {

    List<SubProcesso>findByProcessoId(UUID processoid);
}
