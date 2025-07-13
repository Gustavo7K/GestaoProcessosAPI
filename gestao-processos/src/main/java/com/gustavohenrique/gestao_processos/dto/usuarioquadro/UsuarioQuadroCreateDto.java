package com.gustavohenrique.gestao_processos.dto.usuarioquadro;

import com.gustavohenrique.gestao_processos.entity.UsuarioQuadro;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class UsuarioQuadroCreateDto {
    @NotNull
    private UUID usuarioId;

    @NotNull
    private UUID quadroId;

    @NotNull
    private UsuarioQuadro.Role role;

    public @NotNull UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(@NotNull UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public @NotNull UUID getQuadroId() {
        return quadroId;
    }

    public void setQuadroId(@NotNull UUID quadroId) {
        this.quadroId = quadroId;
    }

    public @NotNull UsuarioQuadro.Role getRole() {
        return role;
    }

    public void setRole(@NotNull UsuarioQuadro.Role role) {
        this.role = role;
    }
}
