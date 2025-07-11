package com.gustavohenrique.gestao_processos.dto.processo;

import com.gustavohenrique.gestao_processos.dto.subprocesso.SubprocessoCreateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ProcessoCreateDto {

    @NotBlank
    @Size(max = 100)
    private String nome;

    private String descricao;

    @Valid
    private List<SubprocessoCreateDto> subprocessos;

    public ProcessoCreateDto(){
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public @NotBlank @Size(max = 100) String getNome() {
        return nome;
    }

    public void setNome(@NotBlank @Size(max = 100) String nome) {
        this.nome = nome;
    }

    public List<SubprocessoCreateDto> getSubprocessos() {
        return subprocessos;
    }

    public void setSubprocessos(List<SubprocessoCreateDto> subprocessos) {
        this.subprocessos = subprocessos;
    }
}
