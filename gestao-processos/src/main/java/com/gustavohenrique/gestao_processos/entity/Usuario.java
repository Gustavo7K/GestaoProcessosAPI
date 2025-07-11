package com.gustavohenrique.gestao_processos.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Usuario {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(columnDefinition = "uuid",
            updatable = false,
            nullable = false)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String senha;

    @OneToMany(mappedBy = "criador", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Quadro> quadrosCriados = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsuarioQuadro> quadroAssociacoes = new HashSet<>();

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Usuario(UUID id, String senha, String email, String nome) {
        this.id = id;
        this.senha = senha;
        this.email = email;
        this.nome = nome;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<UsuarioQuadro> getQuadroAssociacoes() {
        return quadroAssociacoes;
    }

    public void setQuadroAssociacoes(Set<UsuarioQuadro> quadroAssociacoes) {
        this.quadroAssociacoes = quadroAssociacoes;
    }

    public Set<Quadro> getQuadrosCriados() {
        return quadrosCriados;
    }

    public void setQuadrosCriados(Set<Quadro> quadrosCriados) {
        this.quadrosCriados = quadrosCriados;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
