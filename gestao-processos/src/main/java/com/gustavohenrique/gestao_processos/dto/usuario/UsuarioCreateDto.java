package com.gustavohenrique.gestao_processos.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioCreateDto {

    @NotBlank
    @Size(max = 100)
    private String nome;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6)
    private String senha;

    public @NotBlank @Size(max = 100) String getNome() {
        return nome;
    }

    public void setNome(@NotBlank @Size(max = 100) String nome) {
        this.nome = nome;
    }

    public @Email @NotBlank String getEmail() {
        return email;
    }

    public void setEmail(@Email @NotBlank String email) {
        this.email = email;
    }

    public @NotBlank @Size(min = 6) String getSenha() {
        return senha;
    }

    public void setSenha(@NotBlank @Size(min = 6) String senha) {
        this.senha = senha;
    }
}
