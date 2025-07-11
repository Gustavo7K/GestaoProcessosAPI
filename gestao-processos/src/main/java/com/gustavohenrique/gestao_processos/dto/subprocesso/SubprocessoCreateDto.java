package com.gustavohenrique.gestao_processos.dto.subprocesso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SubprocessoCreateDto {

    @NotBlank
    @Size(max = 100)
    private String nome;

    private String descricao;

    public SubprocessoCreateDto() {
    }

    public @NotBlank @Size(max = 100) String getNome() {
        return nome;
    }

    public void setNome(@NotBlank @Size(max = 100) String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
