package com.gustavohenrique.gestao_processos.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavohenrique.gestao_processos.dto.usuario.UsuarioCreateDto;
import com.gustavohenrique.gestao_processos.entity.Usuario;
import com.gustavohenrique.gestao_processos.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")

public class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void deveCadastrarUsuarioComSucesso() throws Exception {
        UsuarioCreateDto dto = new UsuarioCreateDto();
        dto.setNome("Gustavo");
        dto.setEmail("gustavo@email.com");
        dto.setSenha("123456");

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON) // Informa tipo do corpo
                        .content(objectMapper.writeValueAsString(dto))) // Converte para JSON
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Gustavo"))
                .andExpect(jsonPath("$.email").value("gustavo@email.com"));
    }

    @Test
    public void deveBuscarUsuarioPorId() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Ana");
        usuario.setEmail("ana@email.com");
        usuario.setSenha("123456");

        usuario = usuarioRepository.save(usuario); // JPA gera o ID aqui


        mockMvc.perform(get("/usuarios/" + usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana"))
                .andExpect(jsonPath("$.email").value("ana@email.com"));
    }

    @Test
    public void deveRetornarNotFoundAoBuscarUsuarioInexistente() throws Exception {
        UUID idInexistente = UUID.randomUUID();

        mockMvc.perform(get("/usuarios/" + idInexistente))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveBuscarUsuarioPorEmail() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos");
        usuario.setEmail("carlos@email.com");
        usuario.setSenha("123456");

        usuario = usuarioRepository.save(usuario);

        mockMvc.perform(get("/usuarios/email/carlos@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos"))
                .andExpect(jsonPath("$.email").value("carlos@email.com"));
    }

    @Test
    public void deveRetornarNotFoundAoBuscarEmailInexistente() throws Exception {
        mockMvc.perform(get("/usuarios/email/inexistente@email.com"))
                .andExpect(status().isNotFound());
    }

}
