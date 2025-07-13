package com.gustavohenrique.gestao_processos.dto.usuarioquadro;

import com.gustavohenrique.gestao_processos.entity.UsuarioQuadro;

import java.util.UUID;

public record UsuarioQuadroResponseDto(UUID usuarioId,
                                       String nome,
                                       String email,
                                       UsuarioQuadro.Role role){}
