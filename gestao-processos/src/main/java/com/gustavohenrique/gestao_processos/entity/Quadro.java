package com.gustavohenrique.gestao_processos.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.UUID;
import java.util.Set;

@Entity
@Table(name = "quadros")
public class Quadro {
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "criador_id", columnDefinition = "uuid", nullable = false)
    private Usuario criador;

    @OneToMany(mappedBy = "quadro", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsuarioQuadro> participantes = new HashSet<>();

    @OneToMany(mappedBy = "quadro", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Processo> processos = new HashSet<>();

    public Quadro() {
    }

    public Quadro(String nome, Usuario criador) {
        this.nome = nome;
        this.criador = criador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Usuario getCriador() {
        return criador;
    }

    public void setCriador(Usuario criador) {
        this.criador = criador;
    }

    public Set<UsuarioQuadro> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(Set<UsuarioQuadro> participantes) {
        this.participantes = participantes;
    }

    public Set<Processo> getProcessos() {
        return processos;
    }

    public void setProcessos(Set<Processo> processos) {
        this.processos = processos;
    }
}
