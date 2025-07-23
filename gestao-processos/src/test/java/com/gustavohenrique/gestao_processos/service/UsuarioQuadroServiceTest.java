package com.gustavohenrique.gestao_processos.service;

import com.gustavohenrique.gestao_processos.dto.usuarioquadro.UsuarioQuadroResponseDto;
import com.gustavohenrique.gestao_processos.entity.Quadro;
import com.gustavohenrique.gestao_processos.entity.Usuario;
import com.gustavohenrique.gestao_processos.entity.UsuarioQuadro;
import com.gustavohenrique.gestao_processos.repository.UsuarioQuadroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioQuadroServiceTest {
    @Mock
    private UsuarioQuadroRepository usuarioQuadroRepository;

    @InjectMocks
    private UsuarioQuadroService usuarioQuadroService;

    @Test
    public void deveVincularUsuarioComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        Quadro quadro = new Quadro();
        quadro.setId(UUID.randomUUID());

        when(usuarioQuadroRepository.existsByUsuarioIdAndQuadroId(usuario.getId(), quadro.getId()))
                .thenReturn(false);

        usuarioQuadroService.vincularUsuario(usuario, quadro, UsuarioQuadro.Role.EMPLOYEE);

        verify(usuarioQuadroRepository).save(any());
    }

    @Test
    public void deveLancarErroSeUsuarioJaEstaVinculado() {
        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        Quadro quadro = new Quadro();
        quadro.setId(UUID.randomUUID());

        when(usuarioQuadroRepository.existsByUsuarioIdAndQuadroId(usuario.getId(), quadro.getId()))
                .thenReturn(true);

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            usuarioQuadroService.vincularUsuario(usuario, quadro, UsuarioQuadro.Role.ADMIN);
        });

        assertEquals("Usuario já está vinculado a este quadro", e.getMessage());
    }

    @Test
    public void devePromoverUsuarioComSucesso() {
        UUID usuarioId = UUID.randomUUID();
        UUID quadroId = UUID.randomUUID();

        UsuarioQuadro vinculo = new UsuarioQuadro();
        vinculo.setUsuario(new Usuario());
        vinculo.setQuadro(new Quadro());
        vinculo.setRole(UsuarioQuadro.Role.EMPLOYEE);

        when(usuarioQuadroRepository.findByUsuarioIdAndQuadroId(usuarioId, quadroId))
                .thenReturn(Optional.of(vinculo));

        usuarioQuadroService.promoverUsuario(usuarioId, quadroId, UsuarioQuadro.Role.ADMIN);

        assertEquals(UsuarioQuadro.Role.ADMIN, vinculo.getRole());
        verify(usuarioQuadroRepository).save(vinculo);
    }

    @Test
    public void deveLancarErroAoPromoverSeVinculoNaoExiste() {
        UUID usuarioId = UUID.randomUUID();
        UUID quadroId = UUID.randomUUID();

        when(usuarioQuadroRepository.findByUsuarioIdAndQuadroId(usuarioId, quadroId))
                .thenReturn(Optional.empty());

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            usuarioQuadroService.promoverUsuario(usuarioId, quadroId, UsuarioQuadro.Role.ADMIN);
        });

        assertEquals("Usuário não vinculado ao quadro", e.getMessage());
    }

    @Test
    public void deveListarParticipantesDoQuadro() {
        UUID quadroId = UUID.randomUUID();

        Usuario usuario1 = new Usuario();
        usuario1.setId(UUID.randomUUID());
        usuario1.setNome("Ana");
        usuario1.setEmail("ana@email.com");

        UsuarioQuadro vinculo1 = new UsuarioQuadro();
        vinculo1.setUsuario(usuario1);
        vinculo1.setRole(UsuarioQuadro.Role.EMPLOYEE);

        when(usuarioQuadroRepository.findByQuadroId(quadroId))
                .thenReturn(List.of(vinculo1));

        List<UsuarioQuadroResponseDto> resultado = usuarioQuadroService.listarParticipantes(quadroId);

        assertEquals(1, resultado.size());
        assertEquals("Ana", resultado.get(0).nome());
        assertEquals(UsuarioQuadro.Role.EMPLOYEE, resultado.get(0).role());
    }

    @Test
    public void deveVerificarSeUsuarioEhAdmin() {
        UUID usuarioId = UUID.randomUUID();
        UUID quadroId = UUID.randomUUID();

        when(usuarioQuadroRepository.existsByUsuarioIdAndQuadroIdAndRole(usuarioId, quadroId, UsuarioQuadro.Role.ADMIN))
                .thenReturn(true);

        boolean resultado = usuarioQuadroService.isAdmin(usuarioId, quadroId);

        assertTrue(resultado);
    }
}
