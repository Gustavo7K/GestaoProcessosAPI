package dto.subprocesso;

import java.time.Instant;
import java.util.UUID;

public record SubprocessoResponseDto(UUID id,
                                     String nome,
                                     String descricao,
                                     Instant criadoEm){}
