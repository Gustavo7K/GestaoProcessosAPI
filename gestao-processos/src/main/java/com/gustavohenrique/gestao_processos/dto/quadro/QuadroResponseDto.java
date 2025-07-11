package com.gustavohenrique.gestao_processos.dto.quadro;

import java.util.UUID;

public record QuadroResponseDto(UUID id, String nome, String criadorNome) {
}
