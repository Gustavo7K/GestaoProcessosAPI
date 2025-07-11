package dto.processo;

import dto.subprocesso.SubprocessoResponseDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ProcessoResponseDto(UUID id,
                                String nome,
                                String descricao,
                                Instant criadoEm,
                                List<SubprocessoResponseDto> subprocessos) {}
