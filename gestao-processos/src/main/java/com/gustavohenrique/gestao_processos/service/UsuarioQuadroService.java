package com.gustavohenrique.gestao_processos.service;

import com.gustavohenrique.gestao_processos.dto.usuarioquadro.UsuarioQuadroResponseDto;
import com.gustavohenrique.gestao_processos.entity.Quadro;
import com.gustavohenrique.gestao_processos.entity.Usuario;
import com.gustavohenrique.gestao_processos.entity.UsuarioQuadro;
import com.gustavohenrique.gestao_processos.repository.UsuarioQuadroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioQuadroService {
    private final UsuarioQuadroRepository usuarioQuadroRepository;

    //Essa é a dependência que permite acessar o banco de dados para ler, salvar, excluir
    // vínculos entre usuário e quadro (UsuarioQuadro). É injetada via construtor.
    public UsuarioQuadroService(UsuarioQuadroRepository usuarioQuadroRepository){
        this.usuarioQuadroRepository = usuarioQuadroRepository;
    }

    public void vincularUsuario(Usuario usuario, Quadro quadro, UsuarioQuadro.Role role){
        if (usuarioQuadroRepository.existsByUsuarioIdAndQuadroId(usuario.getId(), quadro.getId())){
            throw new IllegalArgumentException("Usuario já está vinculado a este quadro");
        } //verifica se esse vínculo já existe no banco. Se sim, lança uma exceção para impedir inserção duplicada.

        UsuarioQuadro vinculo = new UsuarioQuadro();
        vinculo.setUsuario(usuario);
        vinculo.setQuadro(quadro);
        vinculo.setRole(role);
        usuarioQuadroRepository.save(vinculo);
        //^^Monta um objeto UsuarioQuadro, define os campos e salva no banco, efetivando o vínculo.
    }

    public void promoverUsuario(UUID usuarioId, UUID quadroId, UsuarioQuadro.Role novaRole) {
        UsuarioQuadro vinculo = usuarioQuadroRepository.findByUsuarioIdAndQuadroId(usuarioId, quadroId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não vinculado ao quadro"));
        //Recupera o vínculo existente. Se não encontrar, lança exceção (o usuário não pertence a este quadro).

        vinculo.setRole(novaRole);
        usuarioQuadroRepository.save(vinculo);
    }

    public void removerUsuario(UUID usuarioId, UUID quadroId) {
        usuarioQuadroRepository.deleteByUsuarioIdAndQuadroId(usuarioId, quadroId);
    }

    //Retorna a lista de todos os usuários vinculados a um quadro, transformando em DTOs para exibição no front.
    public List<UsuarioQuadroResponseDto> listarParticipantes(UUID quadroId) {
        List<UsuarioQuadro> vinculos = usuarioQuadroRepository.findByQuadroId(quadroId);
        //Transforma cada vínculo em um DTO com os dados relevantes (nome, email, role) e retorna como lista.
        // Esse métod é essencial para exibir os funcionarios no front
        return vinculos.stream().map(v -> {
            Usuario u = v.getUsuario();
            return new UsuarioQuadroResponseDto(u.getId(), u.getNome(), u.getEmail(), v.getRole());
        }).toList();
    }

    public boolean isAdmin(UUID usuarioId, UUID quadroId) {
        return usuarioQuadroRepository.existsByUsuarioIdAndQuadroIdAndRole(usuarioId, quadroId, UsuarioQuadro.Role.ADMIN);
    }
}
