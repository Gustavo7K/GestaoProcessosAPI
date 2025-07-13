package com.gustavohenrique.gestao_processos.dto.usuario;

import com.gustavohenrique.gestao_processos.entity.UsuarioQuadro;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class PromoverUsuarioDto {
    @NotNull
    private UUID usuarioId;

    @NotNull
    private UUID quadroId;

    @NotNull
    private UsuarioQuadro.Role novaRole;

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

    public @NotNull UsuarioQuadro.Role getNovaRole() {
        return novaRole;
    }

    public void setNovaRole(@NotNull UsuarioQuadro.Role novaRole) {
        this.novaRole = novaRole;
    }
}
