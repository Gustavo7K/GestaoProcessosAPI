package com.gustavohenrique.gestao_processos.service;

import com.gustavohenrique.gestao_processos.dto.quadro.QuadroCreateDto;
import com.gustavohenrique.gestao_processos.entity.Quadro;
import com.gustavohenrique.gestao_processos.entity.Usuario;
import com.gustavohenrique.gestao_processos.entity.UsuarioQuadro;
import com.gustavohenrique.gestao_processos.repository.QuadroRepository;
import com.gustavohenrique.gestao_processos.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuadroServiceTest {
    @Mock
    private QuadroRepository quadroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioQuadroService usuarioQuadroService;

    @InjectMocks
    private QuadroService quadroService;


    @Test
    public void deveCriarQuadroComSucesso(){
        UUID usuarioId = UUID.randomUUID();
        QuadroCreateDto dto = new QuadroCreateDto();
        dto.setNome("Quadro Teste");

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("Gustavo");

        //simula que o usuario ja existe
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        //Simula o save do quadro. Em vez de criar um retorno falso, ele retorna o próprio objeto que
        // foi salvo é útil para manter os dados consistentes no teste.
        when(quadroRepository.save(any())).thenAnswer(invoc -> invoc.getArgument(0));

        Quadro resultado = quadroService.criarQuadro(usuarioId, dto);

        // Assert: verificação do resultado
        assertNotNull(resultado);
        assertEquals("Quadro Teste", resultado.getNome());
        assertEquals(usuario, resultado.getCriador());

        //Verificando a vinculação do usuario ao quadro e se o quadro foi salvo
        verify(usuarioQuadroService).vincularUsuario(usuario, resultado, UsuarioQuadro.Role.ADMIN);
        verify(quadroRepository).save(any());
    }

    @Test
    public void deveLancarErroQuandoUsuarioNaoExiste(){
        UUID usuarioId = UUID.randomUUID();
        QuadroCreateDto dto = new QuadroCreateDto();
        dto.setNome("Quadro Teste");

        //simula se o usuario existe
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->{
            quadroService.criarQuadro(usuarioId, dto);
        });

        assertEquals("Usuario não encontrado", exception.getMessage());
    }

    @Test
    public void deveLancarErroSeQuadroNaoExiste() {
        UUID quadroId = UUID.randomUUID();

        when(quadroRepository.findById(quadroId)).thenReturn(Optional.empty());

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            quadroService.buscarPorId(quadroId);
        });

        assertEquals("Quadro não encontrado", e.getMessage());
    }

    @Test
    public void deveListarQuadrosPorUsuario() {
        UUID usuarioId = UUID.randomUUID();

        Quadro quadro1 = new Quadro();
        Quadro quadro2 = new Quadro();
        //Cria dois quadros simulados e os coloca em uma lista como se fossem os quadros que o usuário possui
        List<Quadro> lista = List.of(quadro1, quadro2);

        when(quadroRepository.findByCriadorId(usuarioId)).thenReturn(lista);

        List<Quadro> resultado = quadroService.listarQuadrosPorUsuario(usuarioId);

        //Verifica se o métod retornou exatamente os dois quadros simulados.
        assertEquals(2, resultado.size());
        verify(quadroRepository).findByCriadorId(usuarioId);
    }

}
