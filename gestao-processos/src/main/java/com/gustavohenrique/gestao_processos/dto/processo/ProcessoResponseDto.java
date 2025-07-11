package com.gustavohenrique.gestao_processos.dto.processo;

import com.gustavohenrique.gestao_processos.dto.subprocesso.SubprocessoResponseDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ProcessoResponseDto(UUID id,
                                String nome,
                                String descricao,
                                Instant criadoEm,
                                List<SubprocessoResponseDto> subprocessos) {}
