package com.gustavohenrique.gestao_processos.dto.usuario;

import java.util.UUID;

public record UsuarioResponseDto(UUID id,
                                 String nome,
                                 String email){}
