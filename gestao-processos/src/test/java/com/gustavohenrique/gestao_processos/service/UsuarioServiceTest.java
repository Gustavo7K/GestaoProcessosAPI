package com.gustavohenrique.gestao_processos.service;

import com.gustavohenrique.gestao_processos.entity.Usuario;
import com.gustavohenrique.gestao_processos.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    public void deveCadastrarUsuarioComSucesso(){
        //Arrange: entrada simulada
        String nome = "Gustavo";
        String email = "gustavo@gmail.com";
        String senha = "123456";

        Usuario usuarioMockado = new Usuario(nome, email, senha);
        when(usuarioRepository.save(any())).thenReturn(usuarioMockado);

        // Act: ação que quero testar
        Usuario resultado = usuarioService.cadastrarUsuario(nome, email, senha);

        // Assert: verificação do resultado
        assertEquals(nome, resultado.getNome());
        assertEquals(email, resultado.getEmail());
        assertEquals(senha, resultado.getSenha());

        verify(usuarioRepository).save(any()); // garante que o métod foi chamado
    }

    @Test
    public void deveLancarExcecaoQuandoEmailJaExiste() {
        String nome = "Gustavo";
        String email = "gustavo@email.com";
        String senha = "123456";

    Usuario jaCadastrado = new Usuario(nome, email, senha);

    //simula que o usuario ja existe
    when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(jaCadastrado));

    // Verifica se o métod lança uma exceção
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        usuarioService.cadastrarUsuario(nome, email, senha);
    });

    assertEquals("Email já cadastrado", exception.getMessage());
    }

    @Test
    public void seIdForValidoRetornaUsuarioCorreto(){
        UUID id = UUID.randomUUID();
        Usuario usuarioMock = new Usuario("Gustavo", "gustavo@gmail.com", "123456");
        usuarioMock.setId(id);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioMock));

        Usuario resultado = usuarioService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Gustavo", resultado.getNome());
        verify(usuarioRepository).findById(id);
    }

    @Test
    public void deveLancarExcecaoQuandoIdNaoExiste(){
        UUID idInexistente = UUID.randomUUID();

        when(usuarioRepository.findById(idInexistente)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->{
            usuarioService.buscarPorId(idInexistente);
        });

        assertEquals("Usuario não encontrado", exception.getMessage());
    }
}
