package com.gustavohenrique.gestao_processos.service;

import com.gustavohenrique.gestao_processos.dto.quadro.QuadroCreateDto;
import com.gustavohenrique.gestao_processos.entity.Quadro;
import com.gustavohenrique.gestao_processos.entity.Usuario;
import com.gustavohenrique.gestao_processos.entity.UsuarioQuadro;
import com.gustavohenrique.gestao_processos.repository.QuadroRepository;
import com.gustavohenrique.gestao_processos.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuadroService {
    private final QuadroRepository quadroRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioQuadroService usuarioQuadroService;

    public QuadroService(QuadroRepository quadroRepository, UsuarioRepository usuarioRepository,
                         UsuarioQuadroService usuarioQuadroService ){
        this.quadroRepository = quadroRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioQuadroService = usuarioQuadroService;
    }

    public Quadro criarQuadro(UUID usuarioId, QuadroCreateDto dto){
        Usuario criador = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario não encontrado"));

        Quadro quadro = new Quadro();
        quadro.setNome(dto.getNome());
        quadro.setCriador(criador);
        quadroRepository.save(quadro);

        usuarioQuadroService.vincularUsuario(criador, quadro, UsuarioQuadro.Role.ADMIN);
        return quadro;
    }

    public Quadro buscarPorId(UUID id){
        return quadroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quadro não encontrado"));
    }

    public List<Quadro> listarQuadrosPorUsuario(UUID usuarioId){
        return quadroRepository.findByUsuario(usuarioId);
    }

    public void deletarQuadro(UUID quadroId, UUID usuarioId) throws IllegalAccessException {
        // Verifica se o usuário é ADMIN no quadro
        boolean isAdmin = usuarioQuadroService.isAdmin(usuarioId, quadroId);
        if (!isAdmin) {
            throw new IllegalAccessException("Você não tem permissão para deletar este quadro"); //todo
        }

        quadroRepository.deleteById(quadroId);
    }
}
